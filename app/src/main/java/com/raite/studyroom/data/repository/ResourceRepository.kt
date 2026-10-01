package com.raite.studyroom.data.repository

import android.content.Context
import android.net.Uri
import com.raite.studyroom.data.local.dao.ResourceDao
import com.raite.studyroom.data.local.toDomain
import com.raite.studyroom.data.local.toEntity
import com.raite.studyroom.data.remote.SupabaseContentSource
import com.raite.studyroom.data.remote.ai.AiProxyClient
import com.raite.studyroom.data.remote.ai.RenameRequest
import com.raite.studyroom.data.remote.dto.ResourceDto
import com.raite.studyroom.data.remote.dto.toDto
import com.raite.studyroom.data.remote.dto.toEntity
import com.raite.studyroom.data.sync.Outbox
import com.raite.studyroom.domain.model.Resource
import com.raite.studyroom.domain.model.SyncState
import com.raite.studyroom.util.AppJson
import com.raite.studyroom.util.AppResult
import com.raite.studyroom.util.Entities
import com.raite.studyroom.util.Ops
import com.raite.studyroom.util.Time
import com.raite.studyroom.util.runCatchingApp
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.encodeToString
import java.io.File
import javax.inject.Inject
import javax.inject.Singleton

/** Resources: upload, offline cache and AI auto-rename (spec sections 3.4, 6). */
@Singleton
class ResourceRepository @Inject constructor(
    @ApplicationContext private val context: Context,
    private val content: SupabaseContentSource,
    private val ai: AiProxyClient,
    private val resourceDao: ResourceDao,
    private val outbox: Outbox,
) {
    companion object {
        const val MAX_BYTES = 10L * 1024 * 1024 // 10 MB (spec section 7)
        private val SUPPORTED = setOf(
            "application/pdf",
            "application/msword",
            "application/vnd.openxmlformats-officedocument.wordprocessingml.document",
            "text/plain",
        )
        fun isSupported(mimeType: String?): Boolean =
            mimeType != null && (mimeType.startsWith("image/") || mimeType in SUPPORTED)
    }

    fun observeResources(roomId: String): Flow<List<Resource>> =
        resourceDao.observeResources(roomId).map { list -> list.map { it.toDomain() } }

    suspend fun refreshResources(roomId: String): AppResult<Unit> = runCatchingApp {
        resourceDao.upsertResources(content.resourcesOf(roomId).map { it.toEntity() })
    }

    /**
     * Uploads a file. Saves a local copy first (offline-first), then uploads.
     * If the upload fails the row is queued and pushed later by SyncWorker.
     */
    suspend fun uploadResource(
        roomId: String,
        userId: String,
        uri: Uri,
        originalName: String,
        mimeType: String?,
        sizeBytes: Long,
    ): AppResult<Resource> {
        if (sizeBytes > MAX_BYTES) return AppResult.Failure("File too large (max 10 MB).")
        if (!isSupported(mimeType)) return AppResult.Failure("Unsupported file type.")
        val bytes = runCatching {
            context.contentResolver.openInputStream(uri)?.use { it.readBytes() }
        }.getOrNull() ?: return AppResult.Failure("Could not read the selected file.")

        val now = Time.nowIso()
        val id = Time.newId()
        val localPath = saveLocal(id, originalName, bytes)
        val storagePath = "$roomId/$id/$originalName"
        val dto = ResourceDto(
            id = id, roomId = roomId, originalName = originalName, displayName = null,
            mimeType = mimeType ?: "application/octet-stream", sizeBytes = sizeBytes,
            storagePath = storagePath, uploadedBy = userId, createdAt = now, updatedAt = now,
        )
        val entity = dto.toEntity().copy(localPath = localPath, syncState = SyncState.SYNCED.name)

        return try {
            content.uploadResource(storagePath, bytes)
            content.upsertResource(dto)
            resourceDao.upsertResource(entity)
            // Best-effort AI rename; never blocks the upload (spec section 3.4).
            runCatching { renameWithAi(entity.toDomain()) }
            AppResult.Success(entity.toDomain())
        } catch (t: Throwable) {
            val queued = entity.copy(syncState = SyncState.PENDING.name)
            resourceDao.upsertResource(queued)
            outbox.enqueue(
                Entities.RESOURCE, id, Ops.UPSERT,
                AppJson.instance.encodeToString(queued.toDto()), roomId,
            )
            AppResult.Success(queued.toDomain())
        }
    }

    /** Downloads the file to app storage and records its local path. */
    suspend fun downloadForOffline(resource: Resource): AppResult<String> = runCatchingApp {
        val existing = resource.localPath?.let { File(it) }
        val file = File(offlineDir(), "${resource.id}-${resource.originalName}")
        val bytes = if (existing != null && existing.exists()) existing.readBytes()
        else content.downloadResource(resource.storagePath)
        file.writeBytes(bytes)
        resourceDao.setLocalPath(resource.id, file.absolutePath)
        file.absolutePath
    }

    /** Asks the proxy to suggest a short, descriptive name (spec section 3.4). */
    suspend fun renameWithAi(resource: Resource): AppResult<String> {
        val suggested = runCatching {
            ai.rename(RenameRequest(resource.roomId, resource.id, resource.originalName))
        }.getOrNull()?.takeIf { it.isNotBlank() }
            ?: return AppResult.Failure("AI rename failed; keeping the original name.")

        resourceDao.setDisplayName(resource.id, suggested)
        val updated = resource.copy(displayName = suggested)
        runCatching { content.upsertResource(updated.toEntity().toDto()) }
        return AppResult.Success(suggested)
    }

    /** Host can edit the AI-suggested name. */
    suspend fun setDisplayName(resource: Resource, name: String): AppResult<Unit> = runCatchingApp {
        resourceDao.setDisplayName(resource.id, name)
        runCatching { content.upsertResource(resource.copy(displayName = name).toEntity().toDto()) }
    }

    suspend fun deleteResource(resource: Resource): AppResult<Unit> = runCatchingApp {
        resource.localPath?.let { runCatching { File(it).takeIf { f -> f.exists() }?.delete() } }
        resourceDao.deleteResource(resource.id)
        outbox.enqueue(
            Entities.RESOURCE, resource.id, Ops.DELETE,
            AppJson.instance.encodeToString(
                resource.copy(deletedAt = Time.nowIso()).toEntity().toDto()
            ),
            resource.roomId,
        )
    }

    private fun offlineDir(): File = File(context.filesDir, "resources").apply { mkdirs() }

    private fun saveLocal(id: String, name: String, bytes: ByteArray): String {
        val file = File(offlineDir(), "$id-$name")
        file.writeBytes(bytes)
        return file.absolutePath
    }
}

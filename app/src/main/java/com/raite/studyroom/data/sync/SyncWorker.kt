package com.raite.studyroom.data.sync

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.raite.studyroom.data.local.dao.OutboxDao
import com.raite.studyroom.data.local.dao.ResourceDao
import com.raite.studyroom.data.local.entity.OutboxEntity
import com.raite.studyroom.data.remote.SupabaseContentSource
import com.raite.studyroom.data.remote.SupabaseDataSource
import com.raite.studyroom.data.remote.dto.QuizDto
import com.raite.studyroom.data.remote.dto.ResourceDto
import com.raite.studyroom.data.remote.dto.ReviewerDto
import com.raite.studyroom.data.remote.dto.RoadmapItemDto
import com.raite.studyroom.data.remote.dto.RoomDto
import com.raite.studyroom.data.remote.dto.TaskDto
import com.raite.studyroom.data.remote.dto.toEntity
import com.raite.studyroom.data.repository.SettingsRepository
import com.raite.studyroom.util.AppJson
import com.raite.studyroom.util.Entities
import com.raite.studyroom.util.Ops
import com.raite.studyroom.util.Time
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.serialization.decodeFromString
import java.io.File

/**
 * Pushes queued outbox writes to Supabase (spec section 6). Runs with a network
 * constraint and exponential backoff; already-synced items are skipped.
 */
@HiltWorker
class SyncWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted params: WorkerParameters,
    private val outboxDao: OutboxDao,
    private val resourceDao: ResourceDao,
    private val content: SupabaseContentSource,
    private val rooms: SupabaseDataSource,
    private val settings: SettingsRepository,
) : CoroutineWorker(appContext, params) {

    private val json = AppJson.instance

    override suspend fun doWork(): Result {
        val pending = outboxDao.pending()
        for (entry in pending) {
            try {
                push(entry)
                outboxDao.delete(entry.id)
            } catch (t: Throwable) {
                outboxDao.bumpAttempts(entry.id)
                return Result.retry() // exponential backoff set by SyncScheduler
            }
        }
        settings.setLastSync(Time.nowIso())
        return Result.success()
    }

    private suspend fun push(entry: OutboxEntity) {
        when (entry.entityType) {
            Entities.TASK ->
                if (entry.operation == Ops.DELETE) content.deleteTask(entry.entityId)
                else content.upsertTask(json.decodeFromString<TaskDto>(entry.payloadJson))

            Entities.ROADMAP -> content.upsertRoadmapItems(
                listOf(json.decodeFromString<RoadmapItemDto>(entry.payloadJson))
            )

            Entities.REVIEWER -> content.upsertReviewer(
                json.decodeFromString<ReviewerDto>(entry.payloadJson)
            )

            Entities.QUIZ -> content.upsertQuiz(
                json.decodeFromString<QuizDto>(entry.payloadJson)
            )

            Entities.ROOM -> rooms.upsertRoom(
                json.decodeFromString<RoomDto>(entry.payloadJson)
            )

            Entities.RESOURCE -> pushResource(entry)
        }
    }

    private suspend fun pushResource(entry: OutboxEntity) {
        val dto: ResourceDto = json.decodeFromString(entry.payloadJson)
        if (entry.operation != Ops.DELETE) {
            val local = resourceDao.getResource(entry.entityId)?.localPath?.let { File(it) }
            if (local != null && local.exists()) {
                content.uploadResource(dto.storagePath, local.readBytes())
            }
        }
        content.upsertResource(dto)
        resourceDao.upsertResource(
            dto.toEntity().copy(
                localPath = resourceDao.getResource(entry.entityId)?.localPath,
                syncState = "SYNCED",
            )
        )
    }
}

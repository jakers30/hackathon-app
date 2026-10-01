package com.raite.studyroom.data.repository

import com.raite.studyroom.data.local.dao.StudyMaterialDao
import com.raite.studyroom.data.local.toDomain
import com.raite.studyroom.data.local.toEntity
import com.raite.studyroom.data.remote.SupabaseContentSource
import com.raite.studyroom.data.remote.ai.AiProxyClient
import com.raite.studyroom.data.remote.ai.GenerateRequest
import com.raite.studyroom.data.remote.ai.ModifyRequest
import com.raite.studyroom.data.remote.ai.RoadmapTopic
import com.raite.studyroom.data.remote.dto.toDto
import com.raite.studyroom.data.remote.dto.toEntity
import com.raite.studyroom.data.sync.Outbox
import com.raite.studyroom.domain.model.Reviewer
import com.raite.studyroom.domain.model.RoadmapItem
import com.raite.studyroom.domain.model.SyncState
import com.raite.studyroom.util.AppJson
import com.raite.studyroom.util.AppResult
import com.raite.studyroom.util.Entities
import com.raite.studyroom.util.Ops
import com.raite.studyroom.util.Time
import com.raite.studyroom.util.runCatchingApp
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.encodeToString
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Study materials: roadmap + reviewer, generated/modified by the AI proxy.
 * Per-student copies (ownerId) so one student's edit never changes it for
 * everyone else (spec section 4, personalization).
 */
@Singleton
class StudyMaterialRepository @Inject constructor(
    private val content: SupabaseContentSource,
    private val ai: AiProxyClient,
    private val dao: StudyMaterialDao,
    private val outbox: Outbox,
) {
    fun observeRoadmap(roomId: String, ownerId: String): Flow<List<RoadmapItem>> =
        dao.observeRoadmap(roomId, ownerId).map { list -> list.map { it.toDomain() } }

    fun observeReviewer(roomId: String, ownerId: String): Flow<Reviewer?> =
        dao.observeReviewer(roomId, ownerId).map { it?.toDomain() }

    suspend fun refresh(roomId: String, ownerId: String): AppResult<Unit> = runCatchingApp {
        dao.upsertRoadmapItems(content.roadmapOf(roomId, ownerId).map { it.toEntity() })
        content.reviewerOf(roomId, ownerId)?.let { dao.upsertReviewer(it.toEntity()) }
    }

    suspend fun generateRoadmap(
        roomId: String,
        ownerId: String,
        prompt: String? = null,
    ): AppResult<List<RoadmapItem>> = try {
        val topics = ai.generateRoadmap(GenerateRequest(roomId = roomId, prompt = prompt))
        if (topics.isEmpty()) {
            AppResult.Failure("The AI returned no roadmap. Add more resources and try again.")
        } else {
            val items = merge(topics, dao.getRoadmap(roomId, ownerId).map { it.toDomain() }, roomId, ownerId)
            persistRoadmap(roomId, ownerId, items)
            AppResult.Success(items)
        }
    } catch (t: Throwable) {
        AppResult.Failure(t.message ?: "Roadmap generation failed. Please retry.", t)
    }

    suspend fun generateReviewer(roomId: String, ownerId: String): AppResult<Reviewer> = try {
        val sections = ai.generateReviewer(GenerateRequest(roomId = roomId))
        if (sections.isEmpty()) {
            AppResult.Failure("The AI returned no reviewer. Add more resources and try again.")
        } else {
            val now = Time.nowIso()
            val existing = dao.getReviewer(roomId, ownerId)?.toDomain()
            val reviewer = Reviewer(
                id = existing?.id ?: Time.newId(), roomId = roomId, ownerId = ownerId,
                sections = sections, createdAt = existing?.createdAt ?: now, updatedAt = now,
            )
            dao.upsertReviewer(reviewer.toEntity())
            runCatching { content.upsertReviewer(reviewer.toEntity().toDto()) }
            AppResult.Success(reviewer)
        }
    } catch (t: Throwable) {
        AppResult.Failure(t.message ?: "Reviewer generation failed. Please retry.", t)
    }

    /** AI modification: room resources + current roadmap + reviewer + the prompt. */
    suspend fun modify(roomId: String, ownerId: String, prompt: String): AppResult<Unit> = try {
        val currentRoadmap = dao.getRoadmap(roomId, ownerId).map { it.toDomain() }
        val currentReviewer = dao.getReviewer(roomId, ownerId)?.toDomain()
        val response = ai.modify(
            ModifyRequest(
                roomId = roomId, prompt = prompt,
                roadmap = currentRoadmap.map { RoadmapTopic(it.topic, it.description, it.subtopics) },
                reviewer = currentReviewer?.sections ?: emptyList(),
            )
        )
        if (response.items.isNotEmpty()) {
            persistRoadmap(roomId, ownerId, merge(response.items, currentRoadmap, roomId, ownerId))
        }
        if (response.sections.isNotEmpty()) {
            val now = Time.nowIso()
            val reviewer = Reviewer(
                id = currentReviewer?.id ?: Time.newId(), roomId = roomId, ownerId = ownerId,
                sections = response.sections, createdAt = currentReviewer?.createdAt ?: now, updatedAt = now,
            )
            dao.upsertReviewer(reviewer.toEntity())
            runCatching { content.upsertReviewer(reviewer.toEntity().toDto()) }
        }
        AppResult.Success(Unit)
    } catch (t: Throwable) {
        AppResult.Failure(t.message ?: "AI request failed. Please retry.", t)
    }

    suspend fun setCompleted(item: RoadmapItem, completed: Boolean): AppResult<Unit> = runCatchingApp {
        val now = Time.nowIso()
        dao.setCompleted(item.id, completed, SyncState.PENDING.name, now)
        outbox.enqueue(
            Entities.ROADMAP, item.id, Ops.UPSERT,
            AppJson.instance.encodeToString(
                item.copy(completed = completed, updatedAt = now).toEntity().toDto()
            ),
            item.roomId,
        )
    }

    private suspend fun persistRoadmap(roomId: String, ownerId: String, items: List<RoadmapItem>) {
        dao.clearRoadmap(roomId, ownerId)
        dao.upsertRoadmapItems(items.map { it.toEntity() })
        runCatching { content.upsertRoadmapItems(items.map { it.toEntity().toDto() }) }
    }

    /** Keeps completion state for topics that already existed. */
    private fun merge(
        topics: List<RoadmapTopic>,
        current: List<RoadmapItem>,
        roomId: String,
        ownerId: String,
    ): List<RoadmapItem> {
        val now = Time.nowIso()
        val done = current.filter { it.completed }.map { it.topic.lowercase() }.toSet()
        return topics.mapIndexed { index, topic ->
            RoadmapItem(
                id = Time.newId(), roomId = roomId, ownerId = ownerId, order = index,
                topic = topic.topic, description = topic.description, subtopics = topic.subtopics,
                completed = topic.topic.lowercase() in done, createdAt = now, updatedAt = now,
            )
        }
    }
}

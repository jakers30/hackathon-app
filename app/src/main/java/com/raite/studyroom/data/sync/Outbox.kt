package com.raite.studyroom.data.sync

import com.raite.studyroom.data.local.dao.OutboxDao
import com.raite.studyroom.data.local.entity.OutboxEntity
import com.raite.studyroom.util.Time
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Outbox pattern (spec section 6). Writes go to Room first, then a row is
 * enqueued here; WorkManager pushes it to Supabase when a connection returns.
 */
@Singleton
class Outbox @Inject constructor(
    private val dao: OutboxDao,
) {
    val pendingCount: Flow<Int> = dao.observePendingCount()

    suspend fun enqueue(
        entityType: String,
        entityId: String,
        operation: String,
        payloadJson: String,
        roomId: String? = null,
    ) {
        dao.enqueue(
            OutboxEntity(
                entityType = entityType,
                entityId = entityId,
                operation = operation,
                payloadJson = payloadJson,
                createdAt = Time.nowIso(),
                roomId = roomId,
            )
        )
    }

    suspend fun clearRoom(roomId: String) = dao.deleteForRoom(roomId)
}

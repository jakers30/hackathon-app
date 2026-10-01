package com.raite.studyroom.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.raite.studyroom.data.local.entity.OutboxEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface OutboxDao {

    @Insert
    suspend fun enqueue(entry: OutboxEntity): Long

    @Query("SELECT * FROM outbox ORDER BY createdAt ASC")
    suspend fun pending(): List<OutboxEntity>

    @Query("SELECT COUNT(*) FROM outbox")
    fun observePendingCount(): Flow<Int>

    @Query("DELETE FROM outbox WHERE id = :id")
    suspend fun delete(id: Long)

    @Query("UPDATE outbox SET attempts = attempts + 1 WHERE id = :id")
    suspend fun bumpAttempts(id: Long)

    @Query("DELETE FROM outbox WHERE roomId = :roomId")
    suspend fun deleteForRoom(roomId: String)
}

package com.raite.studyroom.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.raite.studyroom.data.local.entity.ReviewerEntity
import com.raite.studyroom.data.local.entity.RoadmapItemEntity
import com.raite.studyroom.data.local.entity.RoadmapProgressEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface StudyMaterialDao {

    // ---- roadmap -------------------------------------------------------------

    @Query("SELECT * FROM roadmap_items WHERE roomId = :roomId AND ownerId = :ownerId AND deletedAt IS NULL ORDER BY order_index ASC")
    fun observeRoadmap(roomId: String, ownerId: String): Flow<List<RoadmapItemEntity>>

    @Query("SELECT * FROM roadmap_items WHERE roomId = :roomId AND ownerId = :ownerId AND deletedAt IS NULL ORDER BY order_index ASC")
    suspend fun getRoadmap(roomId: String, ownerId: String): List<RoadmapItemEntity>

    @Upsert
    suspend fun upsertRoadmapItems(items: List<RoadmapItemEntity>)

    @Query("UPDATE roadmap_items SET completed = :completed, syncState = :syncState, updatedAt = :updatedAt WHERE id = :id")
    suspend fun setCompleted(id: String, completed: Boolean, syncState: String, updatedAt: String)

    @Query("DELETE FROM roadmap_items WHERE roomId = :roomId AND ownerId = :ownerId")
    suspend fun clearRoadmap(roomId: String, ownerId: String)

    // ---- roadmap progress (per user, for shared base roadmaps) ----------------

    @Upsert
    suspend fun upsertProgress(progress: List<RoadmapProgressEntity>)

    // ---- reviewer ------------------------------------------------------------

    @Query("SELECT * FROM reviewers WHERE roomId = :roomId AND ownerId = :ownerId AND deletedAt IS NULL LIMIT 1")
    fun observeReviewer(roomId: String, ownerId: String): Flow<ReviewerEntity?>

    @Query("SELECT * FROM reviewers WHERE roomId = :roomId AND ownerId = :ownerId AND deletedAt IS NULL LIMIT 1")
    suspend fun getReviewer(roomId: String, ownerId: String): ReviewerEntity?

    @Upsert
    suspend fun upsertReviewer(reviewer: ReviewerEntity)

    @Query("DELETE FROM reviewers WHERE roomId = :roomId AND ownerId = :ownerId")
    suspend fun clearReviewer(roomId: String, ownerId: String)
}

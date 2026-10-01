package com.raite.studyroom.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.raite.studyroom.data.local.entity.ResourceEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ResourceDao {

    @Query("SELECT * FROM resources WHERE roomId = :roomId AND deletedAt IS NULL ORDER BY createdAt DESC")
    fun observeResources(roomId: String): Flow<List<ResourceEntity>>

    @Query("SELECT * FROM resources WHERE id = :id")
    suspend fun getResource(id: String): ResourceEntity?

    @Upsert
    suspend fun upsertResources(resources: List<ResourceEntity>)

    @Upsert
    suspend fun upsertResource(resource: ResourceEntity)

    @Query("UPDATE resources SET localPath = :path WHERE id = :id")
    suspend fun setLocalPath(id: String, path: String)

    /** Applies the AI-suggested name once the rename call returns. */
    @Query("UPDATE resources SET displayName = :displayName, syncState = :syncState WHERE id = :id")
    suspend fun setDisplayName(id: String, displayName: String, syncState: String = "SYNCED")

    @Query("DELETE FROM resources WHERE id = :id")
    suspend fun deleteResource(id: String)
}

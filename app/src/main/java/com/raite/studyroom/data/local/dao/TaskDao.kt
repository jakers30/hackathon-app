package com.raite.studyroom.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.raite.studyroom.data.local.entity.TaskEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskDao {

    @Query("SELECT * FROM tasks WHERE userId = :userId AND deletedAt IS NULL ORDER BY completed ASC, priority DESC, dueAt ASC")
    fun observeTasks(userId: String): Flow<List<TaskEntity>>

    @Query("SELECT * FROM tasks WHERE id = :id")
    suspend fun getTask(id: String): TaskEntity?

    @Upsert
    suspend fun upsertTask(task: TaskEntity)

    @Upsert
    suspend fun upsertTasks(tasks: List<TaskEntity>)

    @Query("DELETE FROM tasks WHERE id = :id")
    suspend fun deleteTask(id: String)

    /** Clears only rows that are already synced; pending rows stay local. */
    @Query("DELETE FROM tasks WHERE userId = :userId AND syncState = 'SYNCED'")
    suspend fun clearSyncedTasks(userId: String)
}

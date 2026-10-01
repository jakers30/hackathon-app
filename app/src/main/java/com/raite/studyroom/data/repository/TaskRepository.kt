package com.raite.studyroom.data.repository

import com.raite.studyroom.data.local.dao.TaskDao
import com.raite.studyroom.data.local.toDomain
import com.raite.studyroom.data.local.toEntity
import com.raite.studyroom.data.remote.SupabaseContentSource
import com.raite.studyroom.data.remote.dto.toDto
import com.raite.studyroom.data.remote.dto.toEntity
import com.raite.studyroom.data.sync.Outbox
import com.raite.studyroom.domain.model.SyncState
import com.raite.studyroom.domain.model.Task
import com.raite.studyroom.domain.model.TaskPriority
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
 * Personal to-do list (spec section 3.2). Fully editable offline: writes go to
 * Room first and are queued in the outbox for sync (spec section 6).
 */
@Singleton
class TaskRepository @Inject constructor(
    private val content: SupabaseContentSource,
    private val taskDao: TaskDao,
    private val outbox: Outbox,
) {
    fun observeTasks(userId: String): Flow<List<Task>> =
        taskDao.observeTasks(userId).map { list -> list.map { it.toDomain() } }

    suspend fun add(
        userId: String,
        title: String,
        notes: String = "",
        priority: TaskPriority = TaskPriority.MEDIUM,
        dueAt: String? = null,
    ): Task {
        val now = Time.nowIso()
        return Task(
            id = Time.newId(), userId = userId, title = title, notes = notes,
            priority = priority, dueAt = dueAt, completed = false,
            createdAt = now, updatedAt = now, syncState = SyncState.PENDING,
        ).also { save(it) }
    }

    suspend fun update(task: Task): AppResult<Unit> = runCatchingApp {
        save(task.copy(updatedAt = Time.nowIso(), syncState = SyncState.PENDING))
    }

    suspend fun setCompleted(task: Task, completed: Boolean): AppResult<Unit> = runCatchingApp {
        save(task.copy(completed = completed, updatedAt = Time.nowIso(), syncState = SyncState.PENDING))
    }

    suspend fun delete(task: Task): AppResult<Unit> = runCatchingApp {
        taskDao.deleteTask(task.id)
        outbox.enqueue(
            Entities.TASK, task.id, Ops.DELETE,
            AppJson.instance.encodeToString(task.copy(deletedAt = Time.nowIso()).toEntity().toDto()),
        )
    }

    suspend fun refresh(userId: String): AppResult<Unit> = runCatchingApp {
        val remote = content.tasksOf(userId)
        taskDao.clearSyncedTasks(userId)
        taskDao.upsertTasks(remote.map { it.toEntity() })
    }

    private suspend fun save(task: Task) {
        taskDao.upsertTask(task.toEntity())
        outbox.enqueue(
            Entities.TASK, task.id, Ops.UPSERT,
            AppJson.instance.encodeToString(task.toEntity().toDto()),
        )
    }
}

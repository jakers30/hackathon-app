package com.raite.studyroom.ui.screens.tasks

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.raite.studyroom.data.repository.AuthRepository
import com.raite.studyroom.data.repository.TaskRepository
import com.raite.studyroom.domain.model.Task
import com.raite.studyroom.domain.model.TaskPriority
import com.raite.studyroom.util.Time
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TasksViewModel @Inject constructor(
    private val auth: AuthRepository,
    private val tasks: TaskRepository,
) : ViewModel() {

    val userId: String = auth.currentUserId() ?: ""

    val taskList: StateFlow<List<Task>> = tasks.observeTasks(userId)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    var title by mutableStateOf("")
        private set
    var priority by mutableStateOf(TaskPriority.MEDIUM)
        private set
    var dueDate by mutableStateOf("")
        private set

    init {
        refresh()
    }

    fun refresh() {
        viewModelScope.launch { tasks.refresh(userId) }
    }

    fun onTitleChange(value: String) { title = value }
    fun onPriorityChange(value: TaskPriority) { priority = value }
    fun onDueDateChange(value: String) { dueDate = value }

    fun add() {
        if (title.isBlank()) return
        val due = dueDate.toIsoEndOfDay()
        viewModelScope.launch {
            tasks.add(userId, title.trim(), priority = priority, dueAt = due)
            title = ""
            dueDate = ""
            priority = TaskPriority.MEDIUM
        }
    }

    fun toggle(task: Task, completed: Boolean) {
        viewModelScope.launch { tasks.setCompleted(task, completed) }
    }

    fun delete(task: Task) {
        viewModelScope.launch { tasks.delete(task) }
    }
}

/** Accepts "YYYY-MM-DD" and returns an ISO end-of-day timestamp, else null. */
private fun String.toIsoEndOfDay(): String? {
    val trimmed = trim()
    if (trimmed.matches(Regex("\\d{4}-\\d{2}-\\d{2}"))) return "${trimmed}T23:59:59.000Z"
    return null
}

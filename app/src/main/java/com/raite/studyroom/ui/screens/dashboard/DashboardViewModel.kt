package com.raite.studyroom.ui.screens.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.raite.studyroom.data.repository.AuthRepository
import com.raite.studyroom.data.repository.RoomRepository
import com.raite.studyroom.data.repository.TaskRepository
import com.raite.studyroom.domain.model.Room
import com.raite.studyroom.domain.model.Task
import com.raite.studyroom.util.AppResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val auth: AuthRepository,
    private val rooms: RoomRepository,
    private val tasks: TaskRepository,
) : ViewModel() {

    val userId: String = auth.currentUserId() ?: ""
    val userName: String = auth.currentName() ?: "Student"

    val roomList: StateFlow<List<Room>> = rooms.observeMyRooms()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    val taskList: StateFlow<List<Task>> = tasks.observeTasks(userId)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    var lastError: String? = null
        private set

    init {
        refresh()
    }

    fun refresh() {
        viewModelScope.launch {
            auth.syncProfile()
            if (userId.isNotBlank()) {
                rooms.refreshMyRooms(userId)
                tasks.refresh(userId)
            }
        }
    }

    fun addTask(title: String) {
        if (title.isBlank()) return
        viewModelScope.launch {
            tasks.add(userId, title.trim())
            when (val result = tasks.refresh(userId)) {
                is AppResult.Failure -> lastError = result.message
                is AppResult.Success -> lastError = null
            }
        }
    }

    fun toggleTask(task: Task, completed: Boolean) {
        viewModelScope.launch { tasks.setCompleted(task, completed) }
    }

    fun deleteTask(task: Task) {
        viewModelScope.launch { tasks.delete(task) }
    }
}

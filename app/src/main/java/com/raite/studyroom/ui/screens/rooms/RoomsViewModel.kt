package com.raite.studyroom.ui.screens.rooms

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.raite.studyroom.data.repository.AuthRepository
import com.raite.studyroom.data.repository.RoomRepository
import com.raite.studyroom.domain.model.Room
import com.raite.studyroom.util.AppResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RoomsViewModel @Inject constructor(
    private val auth: AuthRepository,
    private val rooms: RoomRepository,
) : ViewModel() {

    val userId: String = auth.currentUserId() ?: ""

    val roomList: StateFlow<List<Room>> = rooms.observeMyRooms()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    var joinCode by mutableStateOf("")
        private set
    var loading by mutableStateOf(false)
        private set
    var message by mutableStateOf<String?>(null)
        private set

    init {
        refresh()
    }

    fun refresh() {
        viewModelScope.launch { rooms.refreshMyRooms(userId) }
    }

    fun onJoinCodeChange(value: String) {
        joinCode = value.uppercase()
        message = null
    }

    fun join(onJoined: (String) -> Unit) {
        if (joinCode.isBlank()) {
            message = "Enter a room code."
            return
        }
        loading = true
        viewModelScope.launch {
            when (val result = rooms.joinByCode(joinCode, userId)) {
                is AppResult.Success -> {
                    joinCode = ""
                    refresh()
                    onJoined(result.data.id)
                }
                is AppResult.Failure -> message = result.message
            }
            loading = false
        }
    }
}

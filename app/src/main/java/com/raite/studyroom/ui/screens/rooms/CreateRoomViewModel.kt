package com.raite.studyroom.ui.screens.rooms

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.raite.studyroom.data.repository.AuthRepository
import com.raite.studyroom.data.repository.RoomRepository
import com.raite.studyroom.domain.model.RoomVisibility
import com.raite.studyroom.util.AppResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CreateRoomViewModel @Inject constructor(
    private val auth: AuthRepository,
    private val rooms: RoomRepository,
) : ViewModel() {

    var name by mutableStateOf("")
        private set
    var description by mutableStateOf("")
        private set
    var visibility by mutableStateOf(RoomVisibility.PRIVATE)
        private set
    var allowedEmails by mutableStateOf("")
        private set
    var loading by mutableStateOf(false)
        private set
    var error by mutableStateOf<String?>(null)
        private set
    var createdRoomId by mutableStateOf<String?>(null)
        private set

    fun onNameChange(value: String) { name = value; error = null }
    fun onDescriptionChange(value: String) { description = value; error = null }
    fun onVisibilityChange(value: RoomVisibility) { visibility = value; error = null }
    fun onAllowedEmailsChange(value: String) { allowedEmails = value; error = null }

    fun create() {
        if (name.isBlank()) {
            error = "Give your room a name."
            return
        }
        val hostId = auth.currentUserId()
        if (hostId == null) {
            error = "You need to be logged in to create a room."
            return
        }
        loading = true
        viewModelScope.launch {
            val emails = allowedEmails.split(',', ' ', '\n').map { it.trim() }.filter { it.isNotEmpty() }
            when (val result = rooms.createRoom(name.trim(), description.trim(), visibility, hostId, emails)) {
                is AppResult.Success -> createdRoomId = result.data.id
                is AppResult.Failure -> error = result.message
            }
            loading = false
        }
    }
}

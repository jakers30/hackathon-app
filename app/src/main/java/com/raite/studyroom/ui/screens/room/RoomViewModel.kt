package com.raite.studyroom.ui.screens.room

import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.raite.studyroom.data.repository.AuthRepository
import com.raite.studyroom.data.repository.QuizRepository
import com.raite.studyroom.data.repository.ResourceRepository
import com.raite.studyroom.data.repository.RoomRepository
import com.raite.studyroom.data.repository.StudyMaterialRepository
import com.raite.studyroom.domain.model.Quiz
import com.raite.studyroom.domain.model.Resource
import com.raite.studyroom.domain.model.Reviewer
import com.raite.studyroom.domain.model.RoadmapItem
import com.raite.studyroom.domain.model.Room
import com.raite.studyroom.domain.model.RoomMember
import com.raite.studyroom.util.AppResult
import com.raite.studyroom.util.FileMeta
import com.raite.studyroom.ui.navigation.Routes
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RoomViewModel @Inject constructor(
    private val auth: AuthRepository,
    private val roomRepo: RoomRepository,
    private val resourceRepo: ResourceRepository,
    private val materialRepo: StudyMaterialRepository,
    private val quizRepo: QuizRepository,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {

    val roomId: String = savedStateHandle.get<String>(Routes.ROOM_ARG).orEmpty()
    val userId: String = auth.currentUserId().orEmpty()

    val room: StateFlow<Room?> = roomRepo.observeRoom(roomId)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), null)

    val members: StateFlow<List<RoomMember>> = roomRepo.observeMembers(roomId)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    val resources: StateFlow<List<Resource>> = resourceRepo.observeResources(roomId)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    val roadmap: StateFlow<List<RoadmapItem>> = materialRepo.observeRoadmap(roomId, userId)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    val reviewer: StateFlow<Reviewer?> = materialRepo.observeReviewer(roomId, userId)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), null)

    val quizzes: StateFlow<List<Quiz>> = quizRepo.observeQuizzes(roomId)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    var isHost by mutableStateOf(false)
        private set
    var busy by mutableStateOf(false)
        private set
    var message by mutableStateOf<String?>(null)
        private set

    init {
        refresh()
    }

    fun refresh() {
        viewModelScope.launch {
            isHost = roomRepo.isHost(roomId, userId)
            roomRepo.refreshMembers(roomId)
            resourceRepo.refreshResources(roomId)
            materialRepo.refresh(roomId, userId)
            quizRepo.refresh(roomId)
        }
    }

    fun clearMessage() { message = null }

    fun toggleRoadmap(item: RoadmapItem, completed: Boolean) {
        viewModelScope.launch { materialRepo.setCompleted(item, completed) }
    }

    fun generateRoadmap() = aiAction("Upload at least one resource first.") {
        materialRepo.generateRoadmap(roomId, userId)
    }

    fun generateReviewer() = aiAction("Upload at least one resource first.") {
        materialRepo.generateReviewer(roomId, userId)
    }

    fun modify(prompt: String) = aiAction("Type a prompt first.") {
        materialRepo.modify(roomId, userId, prompt)
    }

    fun upload(uri: Uri, meta: FileMeta) {
        viewModelScope.launch {
            busy = true
            when (val result = resourceRepo.uploadResource(
                roomId, userId, uri, meta.name, meta.mimeType, meta.size
            )) {
                is AppResult.Success -> message = "Uploaded. The AI will suggest a name."
                is AppResult.Failure -> message = result.message
            }
            busy = false
        }
    }

    fun download(resource: Resource) {
        viewModelScope.launch {
            when (val result = resourceRepo.downloadForOffline(resource)) {
                is AppResult.Success -> message = "Saved for offline use."
                is AppResult.Failure -> message = result.message
            }
        }
    }

    fun deleteResource(resource: Resource) {
        viewModelScope.launch { resourceRepo.deleteResource(resource) }
    }

    private fun aiAction(emptyMessage: String, block: suspend () -> AppResult<*>) {
        if (resources.value.isEmpty()) {
            message = emptyMessage
            return
        }
        viewModelScope.launch {
            busy = true
            when (val result = block()) {
                is AppResult.Success -> message = "Done."
                is AppResult.Failure -> message = result.message
            }
            busy = false
        }
    }

    fun generateQuiz(type: String, questionTypes: List<String>, count: Int, onCreated: (String) -> Unit) {
        viewModelScope.launch {
            busy = true
            when (val result = quizRepo.generate(roomId, userId, type, questionTypes, count)) {
                is AppResult.Success -> onCreated(result.data.id)
                is AppResult.Failure -> message = result.message
            }
            busy = false
        }
    }
}

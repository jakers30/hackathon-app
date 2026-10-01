package com.raite.studyroom.ui.screens.room

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.raite.studyroom.data.repository.QuizRepository
import com.raite.studyroom.domain.model.Quiz
import com.raite.studyroom.domain.model.QuizQuestion
import com.raite.studyroom.util.AppResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class QuizReviewViewModel @Inject constructor(
    private val quizRepo: QuizRepository,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {

    val quizId: String = savedStateHandle.get<String>("quizId").orEmpty()

    val quiz: StateFlow<Quiz?> = quizRepo.observeQuiz(quizId)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), null)

    val questions: StateFlow<List<QuizQuestion>> = quizRepo.observeQuestions(quizId)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    var showAnswers by mutableStateOf(true)
        private set
    var confirmed by mutableStateOf(false)
        private set
    var message by mutableStateOf<String?>(null)
        private set

    fun toggleAnswers() { showAnswers = !showAnswers }

    fun updateConfirmed(value: Boolean) {
        confirmed = value
        viewModelScope.launch { quiz.value?.let { quizRepo.confirmReview(it, value) } }
    }

    fun publish(onPublished: () -> Unit) {
        val current = quiz.value ?: return
        viewModelScope.launch {
            when (val result = quizRepo.publish(current.copy(reviewConfirmed = confirmed))) {
                is AppResult.Success -> onPublished()
                is AppResult.Failure -> message = result.message
            }
        }
    }
}

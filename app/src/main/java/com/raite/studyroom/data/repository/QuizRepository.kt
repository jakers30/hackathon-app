package com.raite.studyroom.data.repository

import com.raite.studyroom.data.local.dao.QuizDao
import com.raite.studyroom.data.local.toDomain
import com.raite.studyroom.data.local.toEntity
import com.raite.studyroom.data.remote.SupabaseContentSource
import com.raite.studyroom.data.remote.ai.AiProxyClient
import com.raite.studyroom.data.remote.ai.QuizRequest
import com.raite.studyroom.data.remote.dto.toDto
import com.raite.studyroom.data.remote.dto.toEntity
import com.raite.studyroom.domain.model.Quiz
import com.raite.studyroom.domain.model.QuizQuestion
import com.raite.studyroom.domain.model.QuizStatus
import com.raite.studyroom.util.AppResult
import com.raite.studyroom.util.Time
import com.raite.studyroom.util.runCatchingApp
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Quiz/exam generator with host review (spec section 3.6). Generated quizzes
 * start as DRAFT; nothing reaches students until the host approves and publishes.
 */
@Singleton
class QuizRepository @Inject constructor(
    private val content: SupabaseContentSource,
    private val ai: AiProxyClient,
    private val quizDao: QuizDao,
) {
    fun observeQuizzes(roomId: String): Flow<List<Quiz>> =
        quizDao.observeQuizzes(roomId).map { list -> list.map { it.toDomain() } }

    fun observeQuiz(quizId: String): Flow<Quiz?> =
        quizDao.observeQuiz(quizId).map { it?.toDomain() }

    fun observeQuestions(quizId: String): Flow<List<QuizQuestion>> =
        quizDao.observeQuestions(quizId).map { list -> list.map { it.toDomain() } }

    suspend fun refresh(roomId: String): AppResult<Unit> = runCatchingApp {
        val quizzes = content.quizzesOf(roomId)
        quizDao.upsertQuizzes(quizzes.map { it.toEntity() })
        quizzes.forEach { quiz ->
            quizDao.upsertQuestions(content.questionsOf(quiz.id).map { it.toEntity() })
        }
    }

    /** Generates a draft quiz. Answer key is stored locally for the host only. */
    suspend fun generate(
        roomId: String,
        userId: String,
        type: String,
        questionTypes: List<String>,
        count: Int,
    ): AppResult<Quiz> = try {
        val response = ai.generateQuiz(
            QuizRequest(roomId, type, questionTypes, count)
        )
        check(response.questions.isNotEmpty()) {
            "The AI produced no questions. Add more resources and try again."
        }
        val now = Time.nowIso()
        val quizId = Time.newId()
        val quiz = Quiz(
            id = quizId, roomId = roomId,
            title = response.title.ifBlank { "Untitled $type" }, type = type,
            status = QuizStatus.DRAFT, questionCount = response.questions.size,
            createdBy = userId, reviewConfirmed = false, createdAt = now, updatedAt = now,
        )
        val questions = response.questions.mapIndexed { index, q ->
            QuizQuestion(
                id = Time.newId(), quizId = quizId, order = index, type = q.type,
                prompt = q.prompt, options = q.options, correctAnswer = q.correctAnswer,
                explanation = q.explanation, sourceResourceName = q.sourceResourceName,
                createdAt = now, updatedAt = now,
            )
        }
        quizDao.upsertQuiz(quiz.toEntity())
        quizDao.upsertQuestions(questions.map { it.toEntity() })
        runCatching {
            content.upsertQuiz(quiz.toEntity().toDto())
            content.upsertQuestions(questions.map { it.toEntity().toDto() })
        }
        AppResult.Success(quiz)
    } catch (t: Throwable) {
        AppResult.Failure(t.message ?: "Quiz generation failed. Please retry.", t)
    }

    /** Host can edit a question before approving. */
    suspend fun updateQuestion(question: QuizQuestion): AppResult<Unit> = runCatchingApp {
        quizDao.upsertQuestion(question.toEntity())
        runCatching { content.upsertQuestions(listOf(question.toEntity().toDto())) }
    }

    /** "I have verified the questions and answer key" checkbox. */
    suspend fun confirmReview(quiz: Quiz, confirmed: Boolean): AppResult<Unit> = runCatchingApp {
        val updated = quiz.copy(reviewConfirmed = confirmed, updatedAt = Time.nowIso())
        quizDao.upsertQuiz(updated.toEntity())
        runCatching { content.upsertQuiz(updated.toEntity().toDto()) }
    }

    /** Publish stays disabled until review is confirmed (spec section 3.6/7). */
    suspend fun publish(quiz: Quiz): AppResult<Unit> = runCatchingApp {
        require(quiz.reviewConfirmed) { "Review the draft before publishing." }
        val now = Time.nowIso()
        quizDao.setStatus(quiz.id, QuizStatus.PUBLISHED.name, true, "SYNCED", now)
        runCatching {
            content.upsertQuiz(quiz.copy(status = QuizStatus.PUBLISHED, updatedAt = now).toEntity().toDto())
        }
    }
}

package com.raite.studyroom.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.raite.studyroom.data.local.entity.QuizEntity
import com.raite.studyroom.data.local.entity.QuizQuestionEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface QuizDao {

    @Query("SELECT * FROM quizzes WHERE roomId = :roomId AND deletedAt IS NULL ORDER BY createdAt DESC")
    fun observeQuizzes(roomId: String): Flow<List<QuizEntity>>

    @Query("SELECT * FROM quizzes WHERE id = :id")
    suspend fun getQuiz(id: String): QuizEntity?

    @Query("SELECT * FROM quizzes WHERE id = :id")
    fun observeQuiz(id: String): Flow<QuizEntity?>

    @Upsert
    suspend fun upsertQuizzes(quizzes: List<QuizEntity>)

    @Upsert
    suspend fun upsertQuiz(quiz: QuizEntity)

    @Query("UPDATE quizzes SET status = :status, reviewConfirmed = :confirmed, syncState = :syncState, updatedAt = :updatedAt WHERE id = :id")
    suspend fun setStatus(id: String, status: String, confirmed: Boolean, syncState: String, updatedAt: String)

    @Query("DELETE FROM quizzes WHERE id = :id")
    suspend fun deleteQuiz(id: String)

    // ---- questions -----------------------------------------------------------

    @Query("SELECT * FROM quiz_questions WHERE quizId = :quizId ORDER BY order_index ASC")
    fun observeQuestions(quizId: String): Flow<List<QuizQuestionEntity>>

    @Upsert
    suspend fun upsertQuestions(questions: List<QuizQuestionEntity>)

    @Upsert
    suspend fun upsertQuestion(question: QuizQuestionEntity)

    @Query("DELETE FROM quiz_questions WHERE quizId = :quizId")
    suspend fun clearQuestions(quizId: String)
}

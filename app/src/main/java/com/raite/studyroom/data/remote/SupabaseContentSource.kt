package com.raite.studyroom.data.remote

import com.raite.studyroom.data.remote.dto.QuizDto
import com.raite.studyroom.data.remote.dto.QuizQuestionDto
import com.raite.studyroom.data.remote.dto.ResourceDto
import com.raite.studyroom.data.remote.dto.ReviewerDto
import com.raite.studyroom.data.remote.dto.RoadmapItemDto
import com.raite.studyroom.data.remote.dto.TaskDto
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.storage.storage
import kotlin.time.Duration.Companion.seconds
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Supabase access for content: resources, study materials, quizzes and tasks,
 * plus Storage upload/download for resource files.
 */
@Singleton
class SupabaseContentSource @Inject constructor(
    private val client: SupabaseClient,
) {
    companion object {
        const val BUCKET = "resources"
    }

    // ---- resources -----------------------------------------------------------

    suspend fun resourcesOf(roomId: String): List<ResourceDto> =
        client.from("resources").select { filter { eq("room_id", roomId) } }.decodeList()

    suspend fun upsertResource(resource: ResourceDto) {
        client.from("resources").upsert(resource)
    }

    suspend fun resourceById(id: String): ResourceDto? =
        client.from("resources").select { filter { eq("id", id) } }.decodeSingleOrNull()

    suspend fun uploadResource(path: String, bytes: ByteArray) {
        client.storage.from(BUCKET).upload(path, bytes)
    }

    suspend fun downloadResource(path: String): ByteArray =
        client.storage.from(BUCKET).downloadAuthenticated(path)

    suspend fun signedResourceUrl(path: String): String =
        client.storage.from(BUCKET).createSignedUrl(path, 3600.seconds)

    // ---- study materials -----------------------------------------------------

    suspend fun roadmapOf(roomId: String, ownerId: String): List<RoadmapItemDto> =
        client.from("roadmap_items")
            .select { filter { eq("room_id", roomId); eq("owner_id", ownerId) } }
            .decodeList()

    suspend fun upsertRoadmapItems(items: List<RoadmapItemDto>) {
        client.from("roadmap_items").upsert(items)
    }

    suspend fun reviewerOf(roomId: String, ownerId: String): ReviewerDto? =
        client.from("reviewers")
            .select { filter { eq("room_id", roomId); eq("owner_id", ownerId) } }
            .decodeSingleOrNull()

    suspend fun upsertReviewer(reviewer: ReviewerDto) {
        client.from("reviewers").upsert(reviewer)
    }

    // ---- quizzes -------------------------------------------------------------

    suspend fun quizzesOf(roomId: String): List<QuizDto> =
        client.from("quizzes").select { filter { eq("room_id", roomId) } }.decodeList()

    suspend fun upsertQuiz(quiz: QuizDto) {
        client.from("quizzes").upsert(quiz)
    }

    suspend fun questionsOf(quizId: String): List<QuizQuestionDto> =
        client.from("quiz_questions").select { filter { eq("quiz_id", quizId) } }.decodeList()

    suspend fun upsertQuestions(questions: List<QuizQuestionDto>) {
        client.from("quiz_questions").upsert(questions)
    }

    // ---- tasks ---------------------------------------------------------------

    suspend fun tasksOf(userId: String): List<TaskDto> =
        client.from("tasks").select { filter { eq("user_id", userId) } }.decodeList()

    suspend fun upsertTask(task: TaskDto) {
        client.from("tasks").upsert(task)
    }

    suspend fun deleteTask(id: String) {
        client.from("tasks").delete { filter { eq("id", id) } }
    }
}

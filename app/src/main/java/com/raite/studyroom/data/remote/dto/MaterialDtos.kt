package com.raite.studyroom.data.remote.dto

import com.raite.studyroom.domain.model.ReviewerSection
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RoadmapItemDto(
    val id: String,
    @SerialName("room_id") val roomId: String,
    @SerialName("owner_id") val ownerId: String,
    @SerialName("order_index") val orderIndex: Int,
    val topic: String,
    val description: String,
    val subtopics: List<String>,
    val completed: Boolean,
    @SerialName("created_at") val createdAt: String,
    @SerialName("updated_at") val updatedAt: String,
    @SerialName("deleted_at") val deletedAt: String? = null,
)

@Serializable
data class ReviewerDto(
    val id: String,
    @SerialName("room_id") val roomId: String,
    @SerialName("owner_id") val ownerId: String,
    val sections: List<ReviewerSection>,
    @SerialName("created_at") val createdAt: String,
    @SerialName("updated_at") val updatedAt: String,
    @SerialName("deleted_at") val deletedAt: String? = null,
)

@Serializable
data class QuizDto(
    val id: String,
    @SerialName("room_id") val roomId: String,
    val title: String,
    val type: String,
    val status: String,
    @SerialName("question_count") val questionCount: Int,
    @SerialName("created_by") val createdBy: String,
    @SerialName("review_confirmed") val reviewConfirmed: Boolean,
    @SerialName("created_at") val createdAt: String,
    @SerialName("updated_at") val updatedAt: String,
    @SerialName("deleted_at") val deletedAt: String? = null,
)

@Serializable
data class QuizQuestionDto(
    val id: String,
    @SerialName("quiz_id") val quizId: String,
    @SerialName("order_index") val orderIndex: Int,
    val type: String,
    val prompt: String,
    val options: List<String>,
    @SerialName("correct_answer") val correctAnswer: String,
    val explanation: String,
    @SerialName("source_resource_id") val sourceResourceId: String? = null,
    @SerialName("source_resource_name") val sourceResourceName: String? = null,
    @SerialName("created_at") val createdAt: String,
    @SerialName("updated_at") val updatedAt: String,
)

@Serializable
data class TaskDto(
    val id: String,
    @SerialName("user_id") val userId: String,
    val title: String,
    val notes: String,
    val priority: String,
    @SerialName("due_at") val dueAt: String? = null,
    val completed: Boolean,
    @SerialName("created_at") val createdAt: String,
    @SerialName("updated_at") val updatedAt: String,
    @SerialName("deleted_at") val deletedAt: String? = null,
)

@Serializable
data class RoadmapProgressDto(
    val id: String,
    @SerialName("item_id") val itemId: String,
    @SerialName("user_id") val userId: String,
    val completed: Boolean,
    @SerialName("updated_at") val updatedAt: String,
)

package com.raite.studyroom.data.remote.ai

import com.raite.studyroom.domain.model.ReviewerSection
import kotlinx.serialization.Serializable

/** Payloads exchanged with the Ktor AI proxy (spec section 10, /ai-proxy). */

@Serializable
data class RenameRequest(
    val roomId: String,
    val resourceId: String,
    val originalName: String,
)

@Serializable
data class RenameResponse(val displayName: String)

@Serializable
data class RoadmapTopic(
    val topic: String,
    val description: String = "",
    val subtopics: List<String> = emptyList(),
)

@Serializable
data class GenerateRequest(
    val roomId: String,
    val prompt: String? = null,
    val roadmap: List<RoadmapTopic> = emptyList(),
    val reviewer: List<ReviewerSection> = emptyList(),
)

@Serializable
data class RoadmapResponse(val items: List<RoadmapTopic> = emptyList())

@Serializable
data class ReviewerResponse(val sections: List<ReviewerSection> = emptyList())

@Serializable
data class ModifyRequest(
    val roomId: String,
    val prompt: String,
    val roadmap: List<RoadmapTopic> = emptyList(),
    val reviewer: List<ReviewerSection> = emptyList(),
)

@Serializable
data class ModifyResponse(
    val items: List<RoadmapTopic> = emptyList(),
    val sections: List<ReviewerSection> = emptyList(),
)

@Serializable
data class QuizRequest(
    val roomId: String,
    val type: String,
    val questionTypes: List<String>,
    val count: Int,
    val topics: List<String> = emptyList(),
)

@Serializable
data class QuizQuestionPayload(
    val type: String,
    val prompt: String,
    val options: List<String> = emptyList(),
    val correctAnswer: String,
    val explanation: String = "",
    val sourceResourceName: String? = null,
)

@Serializable
data class QuizResponse(
    val title: String,
    val questions: List<QuizQuestionPayload> = emptyList(),
    /** Set when the resources were too thin for the requested count (section 3.6). */
    val note: String? = null,
)

@Serializable
data class ApiError(val error: String)

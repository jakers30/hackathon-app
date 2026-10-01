package com.raite.aiproxy

import kotlinx.serialization.Serializable

/** Request/response payloads. These mirror the app's data/remote/ai/AiDtos.kt. */

@Serializable
data class RenameRequest(val roomId: String, val resourceId: String, val originalName: String)

@Serializable
data class RenameResponse(val displayName: String)

@Serializable
data class ReviewerSectionDto(
    val title: String,
    val kind: String,
    val content: List<String> = emptyList(),
)

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
    val reviewer: List<ReviewerSectionDto> = emptyList(),
)

@Serializable
data class RoadmapResponse(val items: List<RoadmapTopic> = emptyList())

@Serializable
data class ReviewerResponse(val sections: List<ReviewerSectionDto> = emptyList())

@Serializable
data class ModifyRequest(
    val roomId: String,
    val prompt: String,
    val roadmap: List<RoadmapTopic> = emptyList(),
    val reviewer: List<ReviewerSectionDto> = emptyList(),
)

@Serializable
data class ModifyResponse(
    val items: List<RoadmapTopic> = emptyList(),
    val sections: List<ReviewerSectionDto> = emptyList(),
)

@Serializable
data class QuizRequest(
    val roomId: String,
    val type: String = "quiz",
    val questionTypes: List<String> = listOf("multiple_choice"),
    val count: Int = 5,
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
    val note: String? = null,
)

@Serializable
data class ApiError(val error: String)

/** A room resource loaded from Supabase, with its bytes for the model. */
data class RoomResource(
    val id: String,
    val name: String,
    val mimeType: String,
    val bytes: ByteArray?,
    val extractedText: String?,
)

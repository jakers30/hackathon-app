package com.raite.studyroom.domain.model

import kotlinx.serialization.Serializable

/**
 * Domain models. These mirror the Supabase tables (spec section 10) and the
 * local Room entities. Timestamps are ISO-8601 UTC strings so they sort
 * correctly and match the Postgres `timestamptz` values without conversion.
 */

data class UserProfile(
    val id: String,
    val name: String,
    val email: String,
    val createdAt: String,
)

data class Room(
    val id: String,
    val name: String,
    val description: String,
    val hostId: String,
    val visibility: RoomVisibility,
    val inviteCode: String,
    val createdAt: String,
    val updatedAt: String,
    val deletedAt: String? = null,
    val syncState: SyncState = SyncState.SYNCED,
)

data class RoomMember(
    val id: String,
    val roomId: String,
    val userId: String,
    val role: MemberRole,
    val joinedAt: String,
    val userName: String? = null,
    val userEmail: String? = null,
)

data class Resource(
    val id: String,
    val roomId: String,
    val originalName: String,
    val displayName: String? = null,
    val mimeType: String,
    val sizeBytes: Long,
    val storagePath: String,
    val localPath: String? = null,
    val uploadedBy: String,
    val createdAt: String,
    val updatedAt: String,
    val deletedAt: String? = null,
    val syncState: SyncState = SyncState.SYNCED,
) {
    /** Name shown in the UI, with the AI-suggested name as the subtitle. */
    val shownName: String get() = displayName?.takeIf { it.isNotBlank() } ?: originalName
}

enum class ReviewerSectionKind(val key: String) {
    CONCEPTS("concepts"),
    DEFINITIONS("definitions"),
    KEY_POINTS("keyPoints"),
    EXAMPLES("examples"),
    FORMULAS("formulas"),
    NOTES("notes");

    companion object {
        fun from(value: String?): ReviewerSectionKind =
            entries.firstOrNull { it.key.equals(value, ignoreCase = true) } ?: NOTES
    }
}

data class RoadmapItem(
    val id: String,
    val roomId: String,
    val ownerId: String,
    val order: Int,
    val topic: String,
    val description: String,
    val subtopics: List<String> = emptyList(),
    val completed: Boolean = false,
    val createdAt: String,
    val updatedAt: String,
    val deletedAt: String? = null,
    val syncState: SyncState = SyncState.SYNCED,
)

@Serializable
data class ReviewerSection(
    val title: String,
    val kind: ReviewerSectionKind,
    val content: List<String>,
)

data class Reviewer(
    val id: String,
    val roomId: String,
    val ownerId: String,
    val sections: List<ReviewerSection>,
    val createdAt: String,
    val updatedAt: String,
    val deletedAt: String? = null,
    val syncState: SyncState = SyncState.SYNCED,
)

data class Quiz(
    val id: String,
    val roomId: String,
    val title: String,
    val type: String,
    val status: QuizStatus,
    val questionCount: Int,
    val createdBy: String,
    val reviewConfirmed: Boolean = false,
    val createdAt: String,
    val updatedAt: String,
    val deletedAt: String? = null,
    val syncState: SyncState = SyncState.SYNCED,
)

data class QuizQuestion(
    val id: String,
    val quizId: String,
    val order: Int,
    val type: String,
    val prompt: String,
    val options: List<String> = emptyList(),
    val correctAnswer: String,
    val explanation: String = "",
    val sourceResourceId: String? = null,
    val sourceResourceName: String? = null,
    val createdAt: String,
    val updatedAt: String,
)

data class Task(
    val id: String,
    val userId: String,
    val title: String,
    val notes: String = "",
    val priority: TaskPriority = TaskPriority.MEDIUM,
    val dueAt: String? = null,
    val completed: Boolean = false,
    val createdAt: String,
    val updatedAt: String,
    val deletedAt: String? = null,
    val syncState: SyncState = SyncState.SYNCED,
)

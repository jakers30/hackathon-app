package com.raite.studyroom.domain.model

/** Room visibility chosen on the Create Room form (spec section 3.3). */
enum class RoomVisibility {
    PUBLIC, PRIVATE, SPECIFIC;

    companion object {
        fun from(value: String?): RoomVisibility =
            entries.firstOrNull { it.name.equals(value, ignoreCase = true) } ?: PRIVATE
    }
}

/** Role a user holds inside a specific room (spec section 4). */
enum class MemberRole {
    HOST, STUDENT;

    companion object {
        fun from(value: String?): MemberRole =
            entries.firstOrNull { it.name.equals(value, ignoreCase = true) } ?: STUDENT
    }
}

/** Local sync state for offline-first rows (spec section 6). */
enum class SyncState {
    SYNCED, PENDING, FAILED;

    companion object {
        fun from(value: String?): SyncState =
            entries.firstOrNull { it.name.equals(value, ignoreCase = true) } ?: SYNCED
    }
}

/** Personal task priority (spec section 3.2). */
enum class TaskPriority {
    LOW, MEDIUM, HIGH;

    companion object {
        fun from(value: String?): TaskPriority =
            entries.firstOrNull { it.name.equals(value, ignoreCase = true) } ?: MEDIUM
    }
}

/** Quiz lifecycle: nothing reaches students until Published (spec section 3.6). */
enum class QuizStatus {
    DRAFT, PUBLISHED;

    companion object {
        fun from(value: String?): QuizStatus =
            entries.firstOrNull { it.name.equals(value, ignoreCase = true) } ?: DRAFT
    }
}

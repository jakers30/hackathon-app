package com.raite.studyroom.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * Local Room (SQLite) entities. These mirror the Supabase tables (spec section 10)
 * and are the single source of truth for the UI (spec section 6).
 *
 * Every synced table carries: client-generated `id` (UUID), `updated_at`,
 * `deleted_at` (soft delete) and a local-only `sync_state`.
 */

@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey val id: String,
    val name: String,
    val email: String,
    val createdAt: String,
)

@Entity(
    tableName = "rooms",
    indices = [Index("hostId"), Index("visibility")],
)
data class RoomEntity(
    @PrimaryKey val id: String,
    val name: String,
    val description: String,
    val hostId: String,
    val visibility: String,
    val inviteCode: String,
    val createdAt: String,
    val updatedAt: String,
    val deletedAt: String? = null,
    val syncState: String = "SYNCED",
)

@Entity(
    tableName = "room_members",
    indices = [Index("roomId"), Index("userId"), Index(value = ["roomId", "userId"], unique = true)],
)
data class RoomMemberEntity(
    @PrimaryKey val id: String,
    val roomId: String,
    val userId: String,
    val role: String,
    val joinedAt: String,
    val updatedAt: String,
    val deletedAt: String? = null,
    val syncState: String = "SYNCED",
)

/** Allowed-user list for rooms with visibility = SPECIFIC (spec section 3.3). */
@Entity(
    tableName = "room_allowed_users",
    indices = [Index("roomId"), Index("userId")],
)
data class RoomAllowedUserEntity(
    @PrimaryKey val id: String,
    val roomId: String,
    val userId: String,
    val email: String,
    val createdAt: String,
    val updatedAt: String,
    val deletedAt: String? = null,
    val syncState: String = "SYNCED",
)

@Entity(
    tableName = "resources",
    indices = [Index("roomId")],
)
data class ResourceEntity(
    @PrimaryKey val id: String,
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
    val syncState: String = "SYNCED",
)

@Entity(
    tableName = "roadmap_items",
    indices = [Index("roomId"), Index("ownerId")],
)
data class RoadmapItemEntity(
    @PrimaryKey val id: String,
    val roomId: String,
    val ownerId: String,
    @ColumnInfo(name = "order_index") val order: Int,
    val topic: String,
    val description: String,
    val subtopicsJson: String = "[]",
    val completed: Boolean = false,
    val createdAt: String,
    val updatedAt: String,
    val deletedAt: String? = null,
    val syncState: String = "SYNCED",
)

@Entity(
    tableName = "roadmap_progress",
    indices = [Index("itemId"), Index("userId"), Index(value = ["itemId", "userId"], unique = true)],
)
data class RoadmapProgressEntity(
    @PrimaryKey val id: String,
    val itemId: String,
    val userId: String,
    val completed: Boolean,
    val updatedAt: String,
    val syncState: String = "SYNCED",
)

@Entity(
    tableName = "reviewers",
    indices = [Index("roomId"), Index("ownerId")],
)
data class ReviewerEntity(
    @PrimaryKey val id: String,
    val roomId: String,
    val ownerId: String,
    val sectionsJson: String = "[]",
    val createdAt: String,
    val updatedAt: String,
    val deletedAt: String? = null,
    val syncState: String = "SYNCED",
)

@Entity(
    tableName = "quizzes",
    indices = [Index("roomId"), Index("status")],
)
data class QuizEntity(
    @PrimaryKey val id: String,
    val roomId: String,
    val title: String,
    val type: String,
    val status: String,
    val questionCount: Int,
    val createdBy: String,
    val reviewConfirmed: Boolean = false,
    val createdAt: String,
    val updatedAt: String,
    val deletedAt: String? = null,
    val syncState: String = "SYNCED",
)

@Entity(
    tableName = "quiz_questions",
    indices = [Index("quizId")],
)
data class QuizQuestionEntity(
    @PrimaryKey val id: String,
    val quizId: String,
    @ColumnInfo(name = "order_index") val order: Int,
    val type: String,
    val prompt: String,
    val optionsJson: String = "[]",
    val correctAnswer: String,
    val explanation: String = "",
    val sourceResourceId: String? = null,
    val sourceResourceName: String? = null,
    val createdAt: String,
    val updatedAt: String,
    val syncState: String = "SYNCED",
)

@Entity(
    tableName = "tasks",
    indices = [Index("userId")],
)
data class TaskEntity(
    @PrimaryKey val id: String,
    val userId: String,
    val title: String,
    val notes: String = "",
    val priority: String = "MEDIUM",
    val dueAt: String? = null,
    val completed: Boolean = false,
    val createdAt: String,
    val updatedAt: String,
    val deletedAt: String? = null,
    val syncState: String = "SYNCED",
)

/** Outbox: queued local writes pushed to Supabase by WorkManager (spec section 6). */
@Entity(tableName = "outbox", indices = [Index("entityType")])
data class OutboxEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val entityType: String,
    val entityId: String,
    val operation: String,
    val payloadJson: String,
    val createdAt: String,
    val attempts: Int = 0,
    /** Room-scoped write (roomId, or null for user-scoped rows like tasks). */
    val roomId: String? = null,
)

package com.raite.studyroom.data.local

import com.raite.studyroom.data.local.entity.ResourceEntity
import com.raite.studyroom.data.local.entity.RoomEntity
import com.raite.studyroom.data.local.entity.RoomMemberEntity
import com.raite.studyroom.data.local.entity.UserEntity
import com.raite.studyroom.domain.model.MemberRole
import com.raite.studyroom.domain.model.Resource
import com.raite.studyroom.domain.model.Room
import com.raite.studyroom.domain.model.RoomMember
import com.raite.studyroom.domain.model.RoomVisibility
import com.raite.studyroom.domain.model.SyncState
import com.raite.studyroom.domain.model.UserProfile
import com.raite.studyroom.data.local.entity.QuizEntity
import com.raite.studyroom.data.local.entity.QuizQuestionEntity
import com.raite.studyroom.data.local.entity.ReviewerEntity
import com.raite.studyroom.data.local.entity.RoadmapItemEntity
import com.raite.studyroom.data.local.entity.TaskEntity
import com.raite.studyroom.domain.model.Quiz
import com.raite.studyroom.domain.model.QuizQuestion
import com.raite.studyroom.domain.model.QuizStatus
import com.raite.studyroom.domain.model.Reviewer
import com.raite.studyroom.domain.model.ReviewerSection
import com.raite.studyroom.domain.model.RoadmapItem
import com.raite.studyroom.domain.model.Task
import com.raite.studyroom.domain.model.TaskPriority
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.json.Json

private val mapJson = Json { ignoreUnknownKeys = true; encodeDefaults = true }

// ---- user -------------------------------------------------------------------
fun UserEntity.toDomain() = UserProfile(id, name, email, createdAt)
fun UserProfile.toEntity() = UserEntity(id, name, email, createdAt)

// ---- room -------------------------------------------------------------------
fun RoomEntity.toDomain() = Room(
    id = id, name = name, description = description, hostId = hostId,
    visibility = RoomVisibility.from(visibility), inviteCode = inviteCode,
    createdAt = createdAt, updatedAt = updatedAt, deletedAt = deletedAt,
    syncState = SyncState.from(syncState),
)

fun Room.toEntity() = RoomEntity(
    id = id, name = name, description = description, hostId = hostId,
    visibility = visibility.name, inviteCode = inviteCode,
    createdAt = createdAt, updatedAt = updatedAt, deletedAt = deletedAt,
    syncState = syncState.name,
)

fun RoomMemberEntity.toDomain() = RoomMember(
    id = id, roomId = roomId, userId = userId, role = MemberRole.from(role),
    joinedAt = joinedAt,
)

// ---- resource ---------------------------------------------------------------
fun ResourceEntity.toDomain() = Resource(
    id = id, roomId = roomId, originalName = originalName, displayName = displayName,
    mimeType = mimeType, sizeBytes = sizeBytes, storagePath = storagePath,
    localPath = localPath, uploadedBy = uploadedBy, createdAt = createdAt,
    updatedAt = updatedAt, deletedAt = deletedAt, syncState = SyncState.from(syncState),
)

fun Resource.toEntity() = ResourceEntity(
    id = id, roomId = roomId, originalName = originalName, displayName = displayName,
    mimeType = mimeType, sizeBytes = sizeBytes, storagePath = storagePath,
    localPath = localPath, uploadedBy = uploadedBy, createdAt = createdAt,
    updatedAt = updatedAt, deletedAt = deletedAt, syncState = syncState.name,
)

// ---- roadmap / reviewer -----------------------------------------------------
fun RoadmapItemEntity.toDomain() = RoadmapItem(
    id = id, roomId = roomId, ownerId = ownerId, order = order, topic = topic,
    description = description, subtopics = decodeStringList(subtopicsJson),
    completed = completed, createdAt = createdAt, updatedAt = updatedAt,
    deletedAt = deletedAt, syncState = SyncState.from(syncState),
)

fun RoadmapItem.toEntity() = RoadmapItemEntity(
    id = id, roomId = roomId, ownerId = ownerId, order = order, topic = topic,
    description = description, subtopicsJson = encodeStringList(subtopics),
    completed = completed, createdAt = createdAt, updatedAt = updatedAt,
    deletedAt = deletedAt, syncState = syncState.name,
)

fun ReviewerEntity.toDomain() = Reviewer(
    id = id, roomId = roomId, ownerId = ownerId, sections = decodeSections(sectionsJson),
    createdAt = createdAt, updatedAt = updatedAt, deletedAt = deletedAt,
    syncState = SyncState.from(syncState),
)

fun Reviewer.toEntity() = ReviewerEntity(
    id = id, roomId = roomId, ownerId = ownerId, sectionsJson = encodeSections(sections),
    createdAt = createdAt, updatedAt = updatedAt, deletedAt = deletedAt,
    syncState = syncState.name,
)

// ---- quiz -------------------------------------------------------------------
fun QuizEntity.toDomain() = Quiz(
    id = id, roomId = roomId, title = title, type = type,
    status = QuizStatus.from(status), questionCount = questionCount,
    createdBy = createdBy, reviewConfirmed = reviewConfirmed, createdAt = createdAt,
    updatedAt = updatedAt, deletedAt = deletedAt, syncState = SyncState.from(syncState),
)

fun Quiz.toEntity() = QuizEntity(
    id = id, roomId = roomId, title = title, type = type, status = status.name,
    questionCount = questionCount, createdBy = createdBy, reviewConfirmed = reviewConfirmed,
    createdAt = createdAt, updatedAt = updatedAt, deletedAt = deletedAt,
    syncState = syncState.name,
)

fun QuizQuestionEntity.toDomain() = QuizQuestion(
    id = id, quizId = quizId, order = order, type = type, prompt = prompt,
    options = decodeStringList(optionsJson), correctAnswer = correctAnswer,
    explanation = explanation, sourceResourceId = sourceResourceId,
    sourceResourceName = sourceResourceName, createdAt = createdAt, updatedAt = updatedAt,
)

fun QuizQuestion.toEntity() = QuizQuestionEntity(
    id = id, quizId = quizId, order = order, type = type, prompt = prompt,
    optionsJson = encodeStringList(options), correctAnswer = correctAnswer,
    explanation = explanation, sourceResourceId = sourceResourceId,
    sourceResourceName = sourceResourceName, createdAt = createdAt, updatedAt = updatedAt,
)

// ---- task -------------------------------------------------------------------
fun TaskEntity.toDomain() = Task(
    id = id, userId = userId, title = title, notes = notes,
    priority = TaskPriority.from(priority), dueAt = dueAt, completed = completed,
    createdAt = createdAt, updatedAt = updatedAt, deletedAt = deletedAt,
    syncState = SyncState.from(syncState),
)

fun Task.toEntity() = TaskEntity(
    id = id, userId = userId, title = title, notes = notes, priority = priority.name,
    dueAt = dueAt, completed = completed, createdAt = createdAt, updatedAt = updatedAt,
    deletedAt = deletedAt, syncState = syncState.name,
)

// ---- json helpers -----------------------------------------------------------
private fun decodeStringList(value: String): List<String> =
    runCatching { mapJson.decodeFromString(ListSerializer(String.serializer()), value) }
        .getOrDefault(emptyList())

private fun encodeStringList(value: List<String>): String =
    mapJson.encodeToString(ListSerializer(String.serializer()), value)

private fun decodeSections(value: String): List<ReviewerSection> =
    runCatching { mapJson.decodeFromString(ListSerializer(ReviewerSection.serializer()), value) }
        .getOrDefault(emptyList())

private fun encodeSections(value: List<ReviewerSection>): String =
    mapJson.encodeToString(ListSerializer(ReviewerSection.serializer()), value)

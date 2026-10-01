package com.raite.studyroom.data.remote.dto

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
import com.raite.studyroom.domain.model.SyncState
import com.raite.studyroom.domain.model.Task
import com.raite.studyroom.domain.model.TaskPriority
import com.raite.studyroom.util.AppJson
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString

private val json get() = AppJson.instance

private fun encodeStrings(v: List<String>) = json.encodeToString(ListSerializer(String.serializer()), v)
private fun decodeStrings(v: String) = runCatching {
    json.decodeFromString(ListSerializer(String.serializer()), v)
}.getOrDefault(emptyList())

private fun encodeSections(v: List<ReviewerSection>) =
    json.encodeToString(ListSerializer(ReviewerSection.serializer()), v)

private fun decodeSections(v: String) = runCatching {
    json.decodeFromString(ListSerializer(ReviewerSection.serializer()), v)
}.getOrDefault(emptyList())

// ---- roadmap ----------------------------------------------------------------
fun RoadmapItemDto.toEntity() = RoadmapItemEntity(
    id = id, roomId = roomId, ownerId = ownerId, order = orderIndex, topic = topic,
    description = description, subtopicsJson = encodeStrings(subtopics), completed = completed,
    createdAt = createdAt, updatedAt = updatedAt, deletedAt = deletedAt,
    syncState = SyncState.SYNCED.name,
)

fun RoadmapItemDto.toDomain() = RoadmapItem(
    id = id, roomId = roomId, ownerId = ownerId, order = orderIndex, topic = topic,
    description = description, subtopics = subtopics, completed = completed,
    createdAt = createdAt, updatedAt = updatedAt, deletedAt = deletedAt,
)

fun RoadmapItemEntity.toDto() = RoadmapItemDto(
    id = id, roomId = roomId, ownerId = ownerId, orderIndex = order, topic = topic,
    description = description, subtopics = decodeStrings(subtopicsJson), completed = completed,
    createdAt = createdAt, updatedAt = updatedAt, deletedAt = deletedAt,
)

// ---- reviewer ---------------------------------------------------------------
fun ReviewerDto.toEntity() = ReviewerEntity(
    id = id, roomId = roomId, ownerId = ownerId, sectionsJson = encodeSections(sections),
    createdAt = createdAt, updatedAt = updatedAt, deletedAt = deletedAt,
    syncState = SyncState.SYNCED.name,
)

fun ReviewerDto.toDomain() = Reviewer(
    id = id, roomId = roomId, ownerId = ownerId, sections = sections,
    createdAt = createdAt, updatedAt = updatedAt, deletedAt = deletedAt,
)

fun ReviewerEntity.toDto() = ReviewerDto(
    id = id, roomId = roomId, ownerId = ownerId, sections = decodeSections(sectionsJson),
    createdAt = createdAt, updatedAt = updatedAt, deletedAt = deletedAt,
)

// ---- quiz -------------------------------------------------------------------
fun QuizDto.toEntity() = QuizEntity(
    id = id, roomId = roomId, title = title, type = type, status = status,
    questionCount = questionCount, createdBy = createdBy, reviewConfirmed = reviewConfirmed,
    createdAt = createdAt, updatedAt = updatedAt, deletedAt = deletedAt,
    syncState = SyncState.SYNCED.name,
)

fun QuizDto.toDomain() = Quiz(
    id = id, roomId = roomId, title = title, type = type, status = QuizStatus.from(status),
    questionCount = questionCount, createdBy = createdBy, reviewConfirmed = reviewConfirmed,
    createdAt = createdAt, updatedAt = updatedAt, deletedAt = deletedAt,
)

fun QuizEntity.toDto() = QuizDto(
    id = id, roomId = roomId, title = title, type = type, status = status,
    questionCount = questionCount, createdBy = createdBy, reviewConfirmed = reviewConfirmed,
    createdAt = createdAt, updatedAt = updatedAt, deletedAt = deletedAt,
)

fun QuizQuestionDto.toEntity() = QuizQuestionEntity(
    id = id, quizId = quizId, order = orderIndex, type = type, prompt = prompt,
    optionsJson = encodeStrings(options), correctAnswer = correctAnswer, explanation = explanation,
    sourceResourceId = sourceResourceId, sourceResourceName = sourceResourceName,
    createdAt = createdAt, updatedAt = updatedAt, syncState = SyncState.SYNCED.name,
)

fun QuizQuestionEntity.toDto() = QuizQuestionDto(
    id = id, quizId = quizId, orderIndex = order, type = type, prompt = prompt,
    options = decodeStrings(optionsJson), correctAnswer = correctAnswer, explanation = explanation,
    sourceResourceId = sourceResourceId, sourceResourceName = sourceResourceName,
    createdAt = createdAt, updatedAt = updatedAt,
)

// ---- task -------------------------------------------------------------------
fun TaskDto.toEntity() = TaskEntity(
    id = id, userId = userId, title = title, notes = notes, priority = priority,
    dueAt = dueAt, completed = completed, createdAt = createdAt, updatedAt = updatedAt,
    deletedAt = deletedAt, syncState = SyncState.SYNCED.name,
)

fun TaskDto.toDomain() = Task(
    id = id, userId = userId, title = title, notes = notes,
    priority = TaskPriority.from(priority), dueAt = dueAt, completed = completed,
    createdAt = createdAt, updatedAt = updatedAt, deletedAt = deletedAt,
)

fun TaskEntity.toDto() = TaskDto(
    id = id, userId = userId, title = title, notes = notes, priority = priority,
    dueAt = dueAt, completed = completed, createdAt = createdAt, updatedAt = updatedAt,
    deletedAt = deletedAt,
)

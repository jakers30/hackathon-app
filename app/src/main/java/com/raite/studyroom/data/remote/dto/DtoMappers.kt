package com.raite.studyroom.data.remote.dto

import com.raite.studyroom.data.local.entity.ResourceEntity
import com.raite.studyroom.data.local.entity.RoomEntity
import com.raite.studyroom.data.local.entity.RoomMemberEntity
import com.raite.studyroom.data.local.entity.UserEntity
import com.raite.studyroom.domain.model.Resource
import com.raite.studyroom.domain.model.Room
import com.raite.studyroom.domain.model.RoomMember
import com.raite.studyroom.domain.model.RoomVisibility
import com.raite.studyroom.domain.model.MemberRole
import com.raite.studyroom.domain.model.SyncState
import com.raite.studyroom.domain.model.UserProfile

// ---- profile / user ---------------------------------------------------------
fun ProfileDto.toEntity() = UserEntity(id, name, email, createdAt)
fun ProfileDto.toDomain() = UserProfile(id, name, email, createdAt)

// ---- room -------------------------------------------------------------------
fun RoomDto.toEntity() = RoomEntity(
    id = id, name = name, description = description, hostId = hostId,
    visibility = visibility, inviteCode = inviteCode, createdAt = createdAt,
    updatedAt = updatedAt, deletedAt = deletedAt, syncState = SyncState.SYNCED.name,
)

fun RoomDto.toDomain() = Room(
    id = id, name = name, description = description, hostId = hostId,
    visibility = RoomVisibility.from(visibility), inviteCode = inviteCode,
    createdAt = createdAt, updatedAt = updatedAt, deletedAt = deletedAt,
)

fun RoomEntity.toDto() = RoomDto(
    id = id, name = name, description = description, hostId = hostId,
    visibility = visibility, inviteCode = inviteCode, createdAt = createdAt,
    updatedAt = updatedAt, deletedAt = deletedAt,
)

fun RoomMemberDto.toEntity() = RoomMemberEntity(
    id = id, roomId = roomId, userId = userId, role = role, joinedAt = joinedAt,
    updatedAt = updatedAt, deletedAt = deletedAt, syncState = SyncState.SYNCED.name,
)

fun RoomMemberDto.toDomain() = RoomMember(
    id = id, roomId = roomId, userId = userId, role = MemberRole.from(role), joinedAt = joinedAt,
)

fun RoomMemberEntity.toDto() = RoomMemberDto(
    id = id, roomId = roomId, userId = userId, role = role, joinedAt = joinedAt,
    updatedAt = updatedAt, deletedAt = deletedAt,
)

// ---- resource ---------------------------------------------------------------
fun ResourceDto.toEntity() = ResourceEntity(
    id = id, roomId = roomId, originalName = originalName, displayName = displayName,
    mimeType = mimeType, sizeBytes = sizeBytes, storagePath = storagePath,
    localPath = null, uploadedBy = uploadedBy, createdAt = createdAt,
    updatedAt = updatedAt, deletedAt = deletedAt, syncState = SyncState.SYNCED.name,
)

fun ResourceDto.toDomain() = Resource(
    id = id, roomId = roomId, originalName = originalName, displayName = displayName,
    mimeType = mimeType, sizeBytes = sizeBytes, storagePath = storagePath,
    localPath = null, uploadedBy = uploadedBy, createdAt = createdAt,
    updatedAt = updatedAt, deletedAt = deletedAt,
)

fun ResourceEntity.toDto() = ResourceDto(
    id = id, roomId = roomId, originalName = originalName, displayName = displayName,
    mimeType = mimeType, sizeBytes = sizeBytes, storagePath = storagePath,
    uploadedBy = uploadedBy, createdAt = createdAt, updatedAt = updatedAt, deletedAt = deletedAt,
)

package com.raite.studyroom.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * DTOs mirror the Supabase tables (spec section 10 / supabase/schema.sql).
 * Required columns have NO default so kotlinx always encodes them on insert.
 */

@Serializable
data class ProfileDto(
    val id: String,
    val name: String,
    val email: String,
    @SerialName("created_at") val createdAt: String,
)

@Serializable
data class RoomDto(
    val id: String,
    val name: String,
    val description: String,
    @SerialName("host_id") val hostId: String,
    val visibility: String,
    @SerialName("invite_code") val inviteCode: String,
    @SerialName("created_at") val createdAt: String,
    @SerialName("updated_at") val updatedAt: String,
    @SerialName("deleted_at") val deletedAt: String? = null,
)

@Serializable
data class RoomMemberDto(
    val id: String,
    @SerialName("room_id") val roomId: String,
    @SerialName("user_id") val userId: String,
    val role: String,
    @SerialName("joined_at") val joinedAt: String,
    @SerialName("updated_at") val updatedAt: String,
    @SerialName("deleted_at") val deletedAt: String? = null,
)

@Serializable
data class RoomAllowedUserDto(
    val id: String,
    @SerialName("room_id") val roomId: String,
    @SerialName("user_id") val userId: String,
    val email: String,
    @SerialName("created_at") val createdAt: String,
    @SerialName("updated_at") val updatedAt: String,
    @SerialName("deleted_at") val deletedAt: String? = null,
)

@Serializable
data class ResourceDto(
    val id: String,
    @SerialName("room_id") val roomId: String,
    @SerialName("original_name") val originalName: String,
    @SerialName("display_name") val displayName: String? = null,
    @SerialName("mime_type") val mimeType: String,
    @SerialName("size_bytes") val sizeBytes: Long,
    @SerialName("storage_path") val storagePath: String,
    @SerialName("uploaded_by") val uploadedBy: String,
    @SerialName("created_at") val createdAt: String,
    @SerialName("updated_at") val updatedAt: String,
    @SerialName("deleted_at") val deletedAt: String? = null,
)

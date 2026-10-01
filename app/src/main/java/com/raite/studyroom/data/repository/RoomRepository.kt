package com.raite.studyroom.data.repository

import com.raite.studyroom.data.local.dao.RoomDao
import com.raite.studyroom.data.local.toDomain
import com.raite.studyroom.data.local.toEntity
import com.raite.studyroom.data.remote.SupabaseDataSource
import com.raite.studyroom.data.remote.dto.RoomAllowedUserDto
import com.raite.studyroom.data.remote.dto.RoomDto
import com.raite.studyroom.data.remote.dto.RoomMemberDto
import com.raite.studyroom.data.remote.dto.toDomain
import com.raite.studyroom.data.remote.dto.toDto
import com.raite.studyroom.data.remote.dto.toEntity
import com.raite.studyroom.domain.model.Room
import com.raite.studyroom.domain.model.RoomMember
import com.raite.studyroom.domain.model.RoomVisibility
import com.raite.studyroom.util.AppResult
import com.raite.studyroom.util.Time
import com.raite.studyroom.util.generateInviteCode
import com.raite.studyroom.util.runCatchingApp
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

/** Rooms: create, join by code, visibility, members (spec sections 3.2-3.3). */
@Singleton
class RoomRepository @Inject constructor(
    private val supabase: SupabaseDataSource,
    private val roomDao: RoomDao,
) {
    fun observeMyRooms(): Flow<List<Room>> =
        roomDao.observeRooms().map { list -> list.map { it.toDomain() } }

    fun observeRoom(roomId: String): Flow<Room?> =
        roomDao.observeRoom(roomId).map { it?.toDomain() }

    fun observeMembers(roomId: String): Flow<List<RoomMember>> =
        roomDao.observeMembers(roomId).map { list -> list.map { it.toDomain() } }

    suspend fun refreshMyRooms(userId: String): AppResult<Unit> = runCatchingApp {
        val remote = supabase.myRooms(userId)
        roomDao.upsertRooms(remote.map { it.toEntity() })
        remote.forEach { room ->
            val members = runCatching { supabase.membersOf(room.id) }.getOrDefault(emptyList())
            roomDao.upsertMembers(members.map { it.toEntity() })
        }
    }

    suspend fun refreshMembers(roomId: String): AppResult<Unit> = runCatchingApp {
        roomDao.upsertMembers(supabase.membersOf(roomId).map { it.toEntity() })
    }

    suspend fun createRoom(
        name: String,
        description: String,
        visibility: RoomVisibility,
        hostId: String,
        allowedEmails: List<String> = emptyList(),
    ): AppResult<Room> = try {
        val now = Time.nowIso()
        val roomId = Time.newId()
        val dto = RoomDto(
            id = roomId, name = name, description = description, hostId = hostId,
            visibility = visibility.name, inviteCode = generateInviteCode(),
            createdAt = now, updatedAt = now,
        )
        supabase.insertRoom(dto)
        val member = RoomMemberDto(Time.newId(), roomId, hostId, "HOST", now, now)
        supabase.insertMember(member)
        if (visibility == RoomVisibility.SPECIFIC) {
            allowedEmails.filter { it.isNotBlank() }.forEach { email ->
                val profile = supabase.findProfileByEmail(email.trim())
                    ?: throw IllegalStateException("No user with that email")
                supabase.upsertAllowedUser(
                    RoomAllowedUserDto(Time.newId(), roomId, profile.id, email.trim(), now, now)
                )
            }
        }
        roomDao.upsertRoom(dto.toEntity())
        roomDao.upsertMember(member.toEntity())
        AppResult.Success(dto.toDomain())
    } catch (t: Throwable) {
        AppResult.Failure(t.message ?: "Could not create room", t)
    }

    suspend fun joinByCode(code: String, userId: String): AppResult<Room> = try {
        val room = requireNotNull(supabase.roomByInviteCode(code.trim().uppercase())) {
            "Room not found. Check the code and try again."
        }
        if (room.visibility == RoomVisibility.SPECIFIC.name) {
            val allowed = supabase.allowedUsersOf(room.id).any { it.userId == userId }
            check(allowed) { "You don't have access to this room." }
        }
        val alreadyMember = supabase.membersOf(room.id).any { it.userId == userId }
        if (!alreadyMember) {
            val now = Time.nowIso()
            val member = RoomMemberDto(Time.newId(), room.id, userId, "STUDENT", now, now)
            supabase.insertMember(member)
            roomDao.upsertMember(member.toEntity())
        }
        roomDao.upsertRoom(room.toEntity())
        AppResult.Success(room.toDomain())
    } catch (t: Throwable) {
        AppResult.Failure(t.message ?: "Could not join room", t)
    }

    suspend fun updateVisibility(
        room: Room,
        visibility: RoomVisibility,
        allowedEmails: List<String> = emptyList(),
    ): AppResult<Unit> = runCatchingApp {
        val updated = room.copy(visibility = visibility, updatedAt = Time.nowIso())
        val entity = updated.toEntity()
        supabase.upsertRoom(entity.toDto())
        roomDao.upsertRoom(entity)
        if (visibility == RoomVisibility.SPECIFIC) {
            val now = Time.nowIso()
            roomDao.clearAllowedUsers(room.id)
            allowedEmails.filter { it.isNotBlank() }.forEach { email ->
                val profile = supabase.findProfileByEmail(email.trim())
                    ?: throw IllegalStateException("No user with that email")
                supabase.upsertAllowedUser(
                    RoomAllowedUserDto(Time.newId(), room.id, profile.id, email.trim(), now, now)
                )
            }
        }
    }

    suspend fun isHost(roomId: String, userId: String): Boolean =
        roomDao.getMembership(roomId, userId)?.role == "HOST"
}

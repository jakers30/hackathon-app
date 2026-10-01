package com.raite.studyroom.data.remote

import com.raite.studyroom.data.remote.dto.ProfileDto
import com.raite.studyroom.data.remote.dto.RoomAllowedUserDto
import com.raite.studyroom.data.remote.dto.RoomDto
import com.raite.studyroom.data.remote.dto.RoomMemberDto
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.providers.builtin.Email
import io.github.jan.supabase.postgrest.from
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.contentOrNull
import kotlinx.serialization.json.jsonPrimitive
import kotlinx.serialization.json.put
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Thin wrapper over supabase-kt for auth, profiles, rooms, members and the
 * allowed-user list. Repositories catch and translate errors.
 */
@Singleton
class SupabaseDataSource @Inject constructor(
    private val client: SupabaseClient,
) {

    // ---- auth ----------------------------------------------------------------

    val sessionStatus = client.auth.sessionStatus

    fun currentUserId(): String? = client.auth.currentUserOrNull()?.id

    fun currentEmail(): String? = client.auth.currentUserOrNull()?.email

    fun currentName(): String? =
        client.auth.currentUserOrNull()?.userMetadata?.get("name")?.jsonPrimitive?.contentOrNull

    suspend fun signIn(email: String, password: String) {
        client.auth.signInWith(Email) {
            this.email = email
            this.password = password
        }
    }

    suspend fun signUp(name: String, email: String, password: String) {
        client.auth.signUpWith(Email) {
            this.email = email
            this.password = password
            data = buildJsonObject { put("name", name) }
        }
    }

    suspend fun signOut() = client.auth.signOut()

    // ---- profiles ------------------------------------------------------------

    suspend fun upsertProfile(profile: ProfileDto) {
        client.from("profiles").upsert(profile)
    }

    suspend fun getProfile(id: String): ProfileDto? =
        client.from("profiles").select { filter { eq("id", id) } }.decodeSingleOrNull()

    suspend fun findProfileByEmail(email: String): ProfileDto? =
        client.from("profiles").select { filter { eq("email", email) } }.decodeSingleOrNull()

    // ---- rooms ---------------------------------------------------------------

    suspend fun insertRoom(room: RoomDto) {
        client.from("rooms").insert(room)
    }

    suspend fun upsertRoom(room: RoomDto) {
        client.from("rooms").upsert(room)
    }

    suspend fun roomById(id: String): RoomDto? =
        client.from("rooms").select { filter { eq("id", id) } }.decodeSingleOrNull()

    suspend fun roomByInviteCode(code: String): RoomDto? =
        client.from("rooms").select { filter { eq("invite_code", code) } }.decodeSingleOrNull()

    suspend fun publicRooms(): List<RoomDto> =
        client.from("rooms").select { filter { eq("visibility", "PUBLIC") } }.decodeList()

    /** Rooms the user belongs to (created or joined, spec section 3.2). */
    suspend fun myRooms(userId: String): List<RoomDto> {
        val memberships = client.from("room_members")
            .select { filter { eq("user_id", userId) } }
            .decodeList<RoomMemberDto>()
        val ids = memberships.map { it.roomId }
        if (ids.isEmpty()) return emptyList()
        return client.from("rooms")
            .select { filter { isIn("id", ids) } }
            .decodeList()
    }

    // ---- members / allowed users ---------------------------------------------

    suspend fun insertMember(member: RoomMemberDto) {
        client.from("room_members").insert(member)
    }

    suspend fun upsertMember(member: RoomMemberDto) {
        client.from("room_members").upsert(member)
    }

    suspend fun membersOf(roomId: String): List<RoomMemberDto> =
        client.from("room_members").select { filter { eq("room_id", roomId) } }.decodeList()

    suspend fun allowedUsersOf(roomId: String): List<RoomAllowedUserDto> =
        client.from("room_allowed_users").select { filter { eq("room_id", roomId) } }.decodeList()

    suspend fun upsertAllowedUser(user: RoomAllowedUserDto) {
        client.from("room_allowed_users").upsert(user)
    }
}

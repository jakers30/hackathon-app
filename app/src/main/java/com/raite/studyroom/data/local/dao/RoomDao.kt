package com.raite.studyroom.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.raite.studyroom.data.local.entity.RoomAllowedUserEntity
import com.raite.studyroom.data.local.entity.RoomEntity
import com.raite.studyroom.data.local.entity.RoomMemberEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface RoomDao {

    @Query("SELECT * FROM rooms WHERE deletedAt IS NULL ORDER BY updatedAt DESC")
    fun observeRooms(): Flow<List<RoomEntity>>

    @Query("SELECT * FROM rooms WHERE id = :id")
    fun observeRoom(id: String): Flow<RoomEntity?>

    @Query("SELECT * FROM rooms WHERE id = :id")
    suspend fun getRoom(id: String): RoomEntity?

    @Upsert
    suspend fun upsertRooms(rooms: List<RoomEntity>)

    @Upsert
    suspend fun upsertRoom(room: RoomEntity)

    @Query("DELETE FROM rooms WHERE id = :id")
    suspend fun deleteRoom(id: String)

    // ---- members -------------------------------------------------------------

    @Query("SELECT * FROM room_members WHERE roomId = :roomId AND deletedAt IS NULL")
    fun observeMembers(roomId: String): Flow<List<RoomMemberEntity>>

    @Query("SELECT * FROM room_members WHERE roomId = :roomId AND userId = :userId LIMIT 1")
    suspend fun getMembership(roomId: String, userId: String): RoomMemberEntity?

    @Upsert
    suspend fun upsertMembers(members: List<RoomMemberEntity>)

    @Upsert
    suspend fun upsertMember(member: RoomMemberEntity)

    // ---- allowed users (visibility = SPECIFIC) --------------------------------

    @Query("SELECT * FROM room_allowed_users WHERE roomId = :roomId AND deletedAt IS NULL")
    fun observeAllowedUsers(roomId: String): Flow<List<RoomAllowedUserEntity>>

    @Upsert
    suspend fun upsertAllowedUsers(users: List<RoomAllowedUserEntity>)

    @Query("DELETE FROM room_allowed_users WHERE roomId = :roomId")
    suspend fun clearAllowedUsers(roomId: String)
}

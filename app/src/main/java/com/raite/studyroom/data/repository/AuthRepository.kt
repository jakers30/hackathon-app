package com.raite.studyroom.data.repository

import com.raite.studyroom.data.local.dao.UserDao
import com.raite.studyroom.data.local.toEntity
import com.raite.studyroom.data.remote.SupabaseDataSource
import com.raite.studyroom.data.remote.dto.ProfileDto
import com.raite.studyroom.domain.model.UserProfile
import com.raite.studyroom.util.AppResult
import com.raite.studyroom.util.Time
import io.github.jan.supabase.auth.status.SessionStatus
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

/** Authentication (spec section 3.1). Passwords are handled by Supabase Auth. */
@Singleton
class AuthRepository @Inject constructor(
    private val supabase: SupabaseDataSource,
    private val userDao: UserDao,
) {
    val sessionStatus: Flow<SessionStatus> = supabase.sessionStatus

    fun currentUserId(): String? = supabase.currentUserId()

    fun currentName(): String? = supabase.currentName()

    suspend fun signIn(email: String, password: String): AppResult<Unit> = runCatchingResult {
        supabase.signIn(email.trim(), password)
        syncProfile()
    }

    suspend fun signUp(name: String, email: String, password: String): AppResult<Unit> = runCatchingResult {
        supabase.signUp(name.trim(), email.trim(), password)
        syncProfile()
    }

    suspend fun signOut() {
        runCatching { supabase.signOut() }
    }

    /** Mirrors the Supabase profile into the local cache so the UI works offline. */
    suspend fun syncProfile() {
        val id = supabase.currentUserId() ?: return
        val existing = runCatching { supabase.getProfile(id) }.getOrNull()
        val now = Time.nowIso()
        val profile = UserProfile(
            id = id,
            name = existing?.name ?: supabase.currentName() ?: "Student",
            email = existing?.email ?: supabase.currentEmail() ?: "",
            createdAt = existing?.createdAt ?: now,
        )
        runCatching { supabase.upsertProfile(profile.toDto()) }
        userDao.upsert(profile.toEntity())
    }

    suspend fun localProfile(id: String): UserProfile? = userDao.getUser(id)?.let {
        UserProfile(it.id, it.name, it.email, it.createdAt)
    }
}

private fun UserProfile.toDto() = ProfileDto(id, name, email, createdAt)

private suspend fun runCatchingResult(block: suspend () -> Unit): AppResult<Unit> =
    try {
        block()
        AppResult.Success(Unit)
    } catch (t: Throwable) {
        AppResult.Failure(t.message ?: "Something went wrong", t)
    }

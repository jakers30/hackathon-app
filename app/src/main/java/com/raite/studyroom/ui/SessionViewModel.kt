package com.raite.studyroom.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.raite.studyroom.data.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.jan.supabase.auth.status.SessionStatus
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

/**
 * Tracks login state. `null` means "still checking the cached session"
 * (spec section 3.1: the session is cached so the app opens offline).
 */
@HiltViewModel
class SessionViewModel @Inject constructor(
    auth: AuthRepository,
) : ViewModel() {

    val isLoggedIn: StateFlow<Boolean?> = auth.sessionStatus
        .map { status ->
            when (status) {
                is SessionStatus.Authenticated -> true
                is SessionStatus.NotAuthenticated -> false
                is SessionStatus.Initializing -> null
                is SessionStatus.RefreshFailure -> auth.currentUserId() != null
            }
        }
        .stateIn(viewModelScope, SharingStarted.Eagerly, null)
}

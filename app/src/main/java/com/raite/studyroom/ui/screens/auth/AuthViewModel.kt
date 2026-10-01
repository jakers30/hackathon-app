package com.raite.studyroom.ui.screens.auth

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.raite.studyroom.data.repository.AuthRepository
import com.raite.studyroom.util.AppResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

data class AuthUiState(
    val isRegister: Boolean = false,
    val name: String = "",
    val email: String = "",
    val password: String = "",
    val loading: Boolean = false,
    val error: String? = null,
    val authenticated: Boolean = false,
)

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val auth: AuthRepository,
) : ViewModel() {

    var state by mutableStateOf(AuthUiState())
        private set

    fun onNameChange(value: String) { state = state.copy(name = value, error = null) }
    fun onEmailChange(value: String) { state = state.copy(email = value, error = null) }
    fun onPasswordChange(value: String) { state = state.copy(password = value, error = null) }

    fun toggleMode() {
        state = state.copy(isRegister = !state.isRegister, error = null)
    }

    fun submit() {
        val s = state
        if (s.email.isBlank() || s.password.isBlank() || (s.isRegister && s.name.isBlank())) {
            state = s.copy(error = "Please fill in all fields.")
            return
        }
        state = s.copy(loading = true, error = null)
        viewModelScope.launch {
            val result = if (s.isRegister) {
                auth.signUp(s.name, s.email, s.password)
            } else {
                auth.signIn(s.email, s.password)
            }
            state = when (result) {
                is AppResult.Success -> {
                    if (auth.currentUserId() != null) state.copy(loading = false, authenticated = true)
                    else state.copy(loading = false, error = "Check your email to confirm your account.")
                }
                is AppResult.Failure -> state.copy(loading = false, error = result.message)
            }
        }
    }
}

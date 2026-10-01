package com.raite.studyroom.ui.screens.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.raite.studyroom.data.repository.AuthRepository
import com.raite.studyroom.data.repository.SettingsRepository
import com.raite.studyroom.data.sync.Outbox
import com.raite.studyroom.data.sync.SyncScheduler
import com.raite.studyroom.domain.model.ThemeMode
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val settings: SettingsRepository,
    private val auth: AuthRepository,
    private val sync: SyncScheduler,
    outbox: Outbox,
) : ViewModel() {

    val themeMode: StateFlow<ThemeMode> = settings.themeMode
        .stateIn(viewModelScope, SharingStarted.Eagerly, ThemeMode.SYSTEM)

    val pendingCount: StateFlow<Int> = outbox.pendingCount
        .stateIn(viewModelScope, SharingStarted.Eagerly, 0)

    val lastSync: StateFlow<String?> = settings.lastSync
        .stateIn(viewModelScope, SharingStarted.Eagerly, null)

    fun setTheme(mode: ThemeMode) {
        viewModelScope.launch { settings.setThemeMode(mode) }
    }

    fun syncNow() = sync.requestSync()

    fun signOut(onDone: () -> Unit) {
        viewModelScope.launch {
            auth.signOut()
            onDone()
        }
    }
}

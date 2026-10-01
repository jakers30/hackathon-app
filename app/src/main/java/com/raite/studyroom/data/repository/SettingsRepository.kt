package com.raite.studyroom.data.repository

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.raite.studyroom.domain.model.ThemeMode
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private val Context.settingsStore by preferencesDataStore(name = "settings")

/** User preferences: theme (spec section 5.3) and last sync time (section 6). */
@Singleton
class SettingsRepository @Inject constructor(
    @ApplicationContext private val context: Context,
) {
    private object Keys {
        val THEME = stringPreferencesKey("theme_mode")
        val LAST_SYNC = stringPreferencesKey("last_sync")
    }

    val themeMode: Flow<ThemeMode> = context.settingsStore.data
        .map { ThemeMode.from(it[Keys.THEME]) }

    val lastSync: Flow<String?> = context.settingsStore.data.map { it[Keys.LAST_SYNC] }

    suspend fun setThemeMode(mode: ThemeMode) {
        context.settingsStore.edit { it[Keys.THEME] = mode.name }
    }

    suspend fun setLastSync(iso: String) {
        context.settingsStore.edit { it[Keys.LAST_SYNC] = iso }
    }
}

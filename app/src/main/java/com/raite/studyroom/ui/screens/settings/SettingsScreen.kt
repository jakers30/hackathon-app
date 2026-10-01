package com.raite.studyroom.ui.screens.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Sync
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.raite.studyroom.domain.model.ThemeMode
import com.raite.studyroom.ui.components.MainScaffold
import com.raite.studyroom.ui.components.OfflineBanner
import com.raite.studyroom.ui.components.SectionHeader
import com.raite.studyroom.ui.navigation.Routes
import com.raite.studyroom.util.toShortDate

@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel,
    onNavigate: (String) -> Unit,
    onSignedOut: () -> Unit,
) {
    val theme by viewModel.themeMode.collectAsStateWithLifecycle()
    val pending by viewModel.pendingCount.collectAsStateWithLifecycle()
    val lastSync by viewModel.lastSync.collectAsStateWithLifecycle()

    MainScaffold(
        currentRoute = Routes.SETTINGS,
        onNavigate = onNavigate,
        topBar = {
            Column {
                OfflineBanner()
                Text(
                    "Settings",
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(16.dp),
                )
            }
        },
    ) { innerPadding ->
        Column(
            Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            SectionHeader("Appearance")
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                ThemeMode.entries.forEach { mode ->
                    androidx.compose.material3.FilterChip(
                        selected = theme == mode,
                        onClick = { viewModel.setTheme(mode) },
                        label = { Text(mode.label()) },
                    )
                }
            }

            HorizontalDivider()

            SectionHeader("Sync")
            Text("Pending items: $pending", style = MaterialTheme.typography.bodyLarge)
            Text(
                lastSync?.let { "Last sync: ${it.toShortDate()}" } ?: "Not synced yet",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            OutlinedButton(onClick = viewModel::syncNow) {
                androidx.compose.material3.Icon(Icons.Rounded.Sync, contentDescription = null)
                Text("Sync now", modifier = Modifier.padding(start = 8.dp))
            }

            HorizontalDivider()

            SectionHeader("Account")
            OutlinedButton(onClick = { viewModel.signOut(onSignedOut) }) {
                Text("Log out")
            }

            Text(
                "Study Room · RAITE 2026 · AI-generated content should always be verified.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}

private fun ThemeMode.label(): String = when (this) {
    ThemeMode.SYSTEM -> "System"
    ThemeMode.LIGHT -> "Light"
    ThemeMode.DARK -> "Dark"
}

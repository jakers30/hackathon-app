package com.raite.studyroom.ui.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Checklist
import androidx.compose.material.icons.rounded.Dashboard
import androidx.compose.material.icons.rounded.MeetingRoom
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import com.raite.studyroom.ui.navigation.Routes

private data class BottomDestination(val route: String, val label: String, val icon: ImageVector)

private val bottomDestinations = listOf(
    BottomDestination(Routes.DASHBOARD, "Dashboard", Icons.Rounded.Dashboard),
    BottomDestination(Routes.ROOMS, "Rooms", Icons.Rounded.MeetingRoom),
    BottomDestination(Routes.TASKS, "Tasks", Icons.Rounded.Checklist),
    BottomDestination(Routes.SETTINGS, "Settings", Icons.Rounded.Settings),
)

/**
 * Bottom navigation shell for the four top-level destinations (spec section 5.2,
 * Compact layout). Teal navigation bar with a restrained indicator.
 */
@Composable
fun MainScaffold(
    currentRoute: String,
    onNavigate: (String) -> Unit,
    modifier: Modifier = Modifier,
    topBar: @Composable () -> Unit = {},
    floatingActionButton: @Composable () -> Unit = {},
    content: @Composable (PaddingValues) -> Unit,
) {
    Scaffold(
        modifier = modifier,
        topBar = topBar,
        floatingActionButton = floatingActionButton,
        bottomBar = {
            NavigationBar(containerColor = MaterialTheme.colorScheme.primary) {
                bottomDestinations.forEach { destination ->
                    val selected = currentRoute == destination.route
                    NavigationBarItem(
                        selected = selected,
                        onClick = { if (!selected) onNavigate(destination.route) },
                        icon = { Icon(destination.icon, contentDescription = destination.label) },
                        label = { androidx.compose.material3.Text(destination.label) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = MaterialTheme.colorScheme.primary,
                            selectedTextColor = MaterialTheme.colorScheme.onPrimary,
                            indicatorColor = MaterialTheme.colorScheme.primaryContainer,
                            unselectedIconColor = MaterialTheme.colorScheme.onPrimary,
                            unselectedTextColor = MaterialTheme.colorScheme.onPrimary,
                        ),
                    )
                }
            }
        },
        content = content,
    )
}

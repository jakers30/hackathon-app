package com.raite.studyroom.ui.screens.dashboard

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.raite.studyroom.ui.components.CtaButton
import com.raite.studyroom.ui.components.MainScaffold
import com.raite.studyroom.ui.components.OfflineBanner
import com.raite.studyroom.ui.components.RoomCard
import com.raite.studyroom.ui.components.SectionHeader
import com.raite.studyroom.ui.components.TaskRow
import com.raite.studyroom.ui.navigation.Routes

@Composable
fun DashboardScreen(
    viewModel: DashboardViewModel,
    onNavigate: (String) -> Unit,
    onOpenRoom: (String) -> Unit,
) {
    val rooms by viewModel.roomList.collectAsStateWithLifecycle()
    val tasks by viewModel.taskList.collectAsStateWithLifecycle()
    var newTask by remember { mutableStateOf("") }

    MainScaffold(
        currentRoute = Routes.DASHBOARD,
        onNavigate = onNavigate,
        topBar = {
            Column {
                OfflineBanner()
                Text(
                    "Hi, ${viewModel.userName}",
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(16.dp),
                )
            }
        },
    ) { innerPadding ->
        LazyColumn(
            Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            // ---- My Tasks ----
            item { SectionHeader("My Tasks") }
            item {
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = newTask,
                        onValueChange = { newTask = it },
                        label = { Text("Add a task") },
                        singleLine = true,
                        modifier = Modifier.weight(1f),
                    )
                    CtaButton(
                        text = "Add",
                        icon = Icons.Rounded.Add,
                        onClick = {
                            viewModel.addTask(newTask)
                            newTask = ""
                        },
                    )
                }
            }
            if (tasks.isEmpty()) {
                item {
                    Text(
                        "No tasks yet. Add your first one above.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            } else {
                items(tasks.take(5), key = { it.id }) { task ->
                    TaskRow(
                        task = task,
                        onToggle = { viewModel.toggleTask(task, it) },
                        onDelete = { viewModel.deleteTask(task) },
                    )
                }
                item {
                    TextButton(onClick = { onNavigate(Routes.TASKS) }) {
                        Text("See all tasks")
                    }
                }
            }

            // ---- My Rooms ----
            item { SectionHeader("My Rooms") }
            item {
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    CtaButton(
                        text = "Create Room",
                        icon = Icons.Rounded.Add,
                        onClick = { onNavigate(Routes.CREATE_ROOM) },
                    )
                    OutlinedButton(onClick = { onNavigate(Routes.ROOMS) }) {
                        Text("Join with Code")
                    }
                }
            }
            if (rooms.isEmpty()) {
                item {
                    Text(
                        "No rooms yet. Create one or join with a code.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            } else {
                items(rooms, key = { it.id }) { room ->
                    RoomCard(room = room, onClick = { onOpenRoom(room.id) })
                }
            }
        }
    }
}

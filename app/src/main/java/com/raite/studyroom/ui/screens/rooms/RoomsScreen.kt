package com.raite.studyroom.ui.screens.rooms

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
import androidx.compose.material.icons.rounded.MeetingRoom
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.raite.studyroom.ui.components.CtaButton
import com.raite.studyroom.ui.components.EmptyState
import com.raite.studyroom.ui.components.MainScaffold
import com.raite.studyroom.ui.components.OfflineBanner
import com.raite.studyroom.ui.components.RoomCard
import com.raite.studyroom.ui.navigation.Routes

@Composable
fun RoomsScreen(
    viewModel: RoomsViewModel,
    onNavigate: (String) -> Unit,
    onOpenRoom: (String) -> Unit,
) {
    val rooms by viewModel.roomList.collectAsStateWithLifecycle()

    MainScaffold(
        currentRoute = Routes.ROOMS,
        onNavigate = onNavigate,
        topBar = {
            Column {
                OfflineBanner()
                Text(
                    "My Rooms",
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
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            item {
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = viewModel.joinCode,
                        onValueChange = viewModel::onJoinCodeChange,
                        label = { Text("Room code") },
                        singleLine = true,
                        modifier = Modifier.weight(1f),
                    )
                    CtaButton(
                        text = "Join",
                        onClick = { viewModel.join(onOpenRoom) },
                        enabled = !viewModel.loading,
                    )
                }
            }
            viewModel.message?.let { message ->
                item {
                    Text(message, color = MaterialTheme.colorScheme.error)
                }
            }
            item {
                CtaButton(
                    text = "Create Room",
                    icon = Icons.Rounded.Add,
                    onClick = { onNavigate(Routes.CREATE_ROOM) },
                )
            }
            if (viewModel.loading) {
                item { CircularProgressIndicator() }
            }
            if (rooms.isEmpty()) {
                item {
                    EmptyState(
                        icon = Icons.Rounded.MeetingRoom,
                        message = "No rooms yet. Create one or join with a code.",
                        actionLabel = "Create Room",
                        onAction = { onNavigate(Routes.CREATE_ROOM) },
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

package com.raite.studyroom.ui.screens.rooms

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.raite.studyroom.domain.model.RoomVisibility
import com.raite.studyroom.ui.components.CtaButton
import com.raite.studyroom.ui.components.label

@Composable
fun CreateRoomScreen(
    viewModel: CreateRoomViewModel,
    onBack: () -> Unit,
    onCreated: (String) -> Unit,
) {
    LaunchedEffect(viewModel.createdRoomId) {
        viewModel.createdRoomId?.let(onCreated)
    }

    Column(
        Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Row {
            IconButton(onClick = onBack) {
                Icon(Icons.AutoMirrored.Rounded.ArrowBack, contentDescription = "Back")
            }
            Text(
                "Create Room",
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(top = 8.dp),
            )
        }

        OutlinedTextField(
            value = viewModel.name,
            onValueChange = viewModel::onNameChange,
            label = { Text("Room name") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
        )

        OutlinedTextField(
            value = viewModel.description,
            onValueChange = viewModel::onDescriptionChange,
            label = { Text("Description") },
            modifier = Modifier.fillMaxWidth(),
        )

        Text("Visibility", style = MaterialTheme.typography.titleMedium)
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            RoomVisibility.entries.forEach { option ->
                FilterChip(
                    selected = viewModel.visibility == option,
                    onClick = { viewModel.onVisibilityChange(option) },
                    label = { Text(option.label()) },
                )
            }
        }

        if (viewModel.visibility == RoomVisibility.SPECIFIC) {
            OutlinedTextField(
                value = viewModel.allowedEmails,
                onValueChange = viewModel::onAllowedEmailsChange,
                label = { Text("Allowed emails (comma separated)") },
                modifier = Modifier.fillMaxWidth(),
            )
        }

        Text(
            "You can add resources and attach files after the room is created.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )

        viewModel.error?.let { Text(it, color = MaterialTheme.colorScheme.error) }

        CtaButton(
            text = "Create Room",
            onClick = viewModel::create,
            enabled = !viewModel.loading,
            modifier = Modifier.fillMaxWidth(),
        )
    }
}

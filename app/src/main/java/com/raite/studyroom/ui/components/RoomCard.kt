package com.raite.studyroom.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Group
import androidx.compose.material.icons.rounded.Lock
import androidx.compose.material.icons.rounded.Public
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.raite.studyroom.domain.model.Room
import com.raite.studyroom.domain.model.RoomVisibility

/** Card used in My Rooms / Browse Public Rooms. */
@Composable
fun RoomCard(
    room: Room,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier.fillMaxWidth().clickable(onClick = onClick),
    ) {
        Column(Modifier.padding(16.dp)) {
            Text(room.name, style = MaterialTheme.typography.titleMedium)
            if (room.description.isNotBlank()) {
                Text(
                    room.description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(top = 4.dp),
                )
            }
            Row(Modifier.padding(top = 8.dp)) {
                AssistChip(
                    onClick = {},
                    label = { Text(room.visibility.label()) },
                    leadingIcon = {
                        Icon(
                            room.visibility.icon(),
                            contentDescription = null,
                            modifier = Modifier.padding(0.dp),
                        )
                    },
                )
                Text(
                    "Code: ${room.inviteCode}",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(start = 12.dp, top = 12.dp),
                )
            }
        }
    }
}

fun RoomVisibility.label(): String = when (this) {
    RoomVisibility.PUBLIC -> "Public"
    RoomVisibility.PRIVATE -> "Private"
    RoomVisibility.SPECIFIC -> "Specific users"
}

fun RoomVisibility.icon(): ImageVector = when (this) {
    RoomVisibility.PUBLIC -> Icons.Rounded.Public
    RoomVisibility.PRIVATE -> Icons.Rounded.Lock
    RoomVisibility.SPECIFIC -> Icons.Rounded.Group
}

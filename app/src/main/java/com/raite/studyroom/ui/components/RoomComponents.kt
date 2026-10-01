package com.raite.studyroom.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.rounded.Download
import androidx.compose.material.icons.automirrored.rounded.InsertDriveFile
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.raite.studyroom.domain.model.Resource
import com.raite.studyroom.domain.model.RoadmapItem
import com.raite.studyroom.domain.model.SyncState
import com.raite.studyroom.util.toShortDate

/** A single resource row (spec section 3.4A). */
@Composable
fun ResourceItem(
    resource: Resource,
    isHost: Boolean,
    onDownload: () -> Unit,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Card(modifier.fillMaxWidth()) {
        Row(Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.AutoMirrored.Rounded.InsertDriveFile, contentDescription = null)
            Column(Modifier.weight(1f).padding(start = 12.dp)) {
                Text(resource.shownName, style = MaterialTheme.typography.bodyLarge)
                val aiRenamed = resource.displayName?.isNotBlank() == true &&
                    resource.displayName != resource.originalName
                Text(
                    text = (if (aiRenamed) "Originally: ${resource.originalName} · " else "") +
                        resource.createdAt.toShortDate(),
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
                if (resource.syncState != SyncState.SYNCED) {
                    SyncIndicator(resource.syncState)
                }
            }
            IconButton(onClick = onDownload) {
                Icon(Icons.Rounded.Download, contentDescription = "Download for offline")
            }
            if (isHost) {
                IconButton(onClick = onDelete) {
                    Icon(Icons.Rounded.Delete, contentDescription = "Delete resource")
                }
            }
        }
    }
}

/** Roadmap item with completion tracking (spec section 3.4B). */
@Composable
fun RoadmapCard(
    item: RoadmapItem,
    onToggle: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    Card(modifier.fillMaxWidth()) {
        Row(Modifier.padding(8.dp), verticalAlignment = Alignment.Top) {
            Checkbox(checked = item.completed, onCheckedChange = onToggle)
            Column(Modifier.weight(1f).padding(top = 8.dp)) {
                Text(
                    "${item.order + 1}. ${item.topic}",
                    style = MaterialTheme.typography.titleMedium,
                    textDecoration = if (item.completed) TextDecoration.LineThrough else null,
                )
                if (item.description.isNotBlank()) {
                    Text(
                        item.description,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
                if (item.subtopics.isNotEmpty()) {
                    Text(
                        item.subtopics.joinToString(" · "),
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.padding(top = 4.dp),
                    )
                }
            }
        }
    }
}

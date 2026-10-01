package com.raite.studyroom.ui.screens.room

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AutoAwesome
import androidx.compose.material.icons.automirrored.rounded.InsertDriveFile
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.raite.studyroom.domain.model.Resource
import com.raite.studyroom.domain.model.Reviewer
import com.raite.studyroom.domain.model.RoadmapItem
import com.raite.studyroom.ui.components.AiGeneratedLabel
import com.raite.studyroom.ui.components.CtaButton
import com.raite.studyroom.ui.components.EmptyState
import com.raite.studyroom.ui.components.ResourceItem
import com.raite.studyroom.ui.components.ReviewerSection
import com.raite.studyroom.ui.components.RoadmapCard
import com.raite.studyroom.ui.components.SectionHeader

@Composable
fun ResourcesTab(
    resources: List<Resource>,
    isHost: Boolean,
    onDownload: (Resource) -> Unit,
    onDelete: (Resource) -> Unit,
) {
    LazyColumn(
        Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        item {
            Text(
                "Only upload materials you own or have permission to share.",
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
        if (resources.isEmpty()) {
            item {
                EmptyState(
                    icon = Icons.AutoMirrored.Rounded.InsertDriveFile,
                    message = "No resources yet." +
                        if (isHost) " Tap + to upload a PDF, document or image." else " The host hasn't uploaded any yet.",
                )
            }
        } else {
            items(resources, key = { it.id }) { resource ->
                ResourceItem(
                    resource = resource,
                    isHost = isHost,
                    onDownload = { onDownload(resource) },
                    onDelete = { onDelete(resource) },
                )
            }
        }
    }
}

@Composable
fun MaterialsTab(
    roadmap: List<RoadmapItem>,
    reviewer: Reviewer?,
    online: Boolean,
    busy: Boolean,
    onToggle: (RoadmapItem, Boolean) -> Unit,
    onGenerateRoadmap: () -> Unit,
    onGenerateReviewer: () -> Unit,
) {
    LazyColumn(
        Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        item {
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                CtaButton(
                    text = "Generate Roadmap",
                    icon = Icons.Rounded.AutoAwesome,
                    onClick = onGenerateRoadmap,
                    enabled = online && !busy,
                )
                OutlinedButton(onClick = onGenerateReviewer, enabled = online && !busy) {
                    Text("Generate Reviewer")
                }
            }
        }
        if (!online) {
            item {
                Text(
                    "Connect to the internet to use AI.",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }
        if (roadmap.isNotEmpty() || reviewer != null) {
            item { AiGeneratedLabel() }
        }
        item { SectionHeader("Study Roadmap") }
        if (roadmap.isEmpty()) {
            item {
                Text(
                    "No roadmap yet. Generate one from this room's resources.",
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        } else {
            items(roadmap, key = { it.id }) { item ->
                RoadmapCard(item = item, onToggle = { onToggle(item, it) })
            }
        }
        item { SectionHeader("Reviewer") }
        val sections = reviewer?.sections.orEmpty()
        if (sections.isEmpty()) {
            item {
                Text(
                    "No reviewer yet. Generate one to see key concepts, definitions and formulas.",
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        } else {
            items(sections) { section -> ReviewerSection(section) }
        }
    }
}

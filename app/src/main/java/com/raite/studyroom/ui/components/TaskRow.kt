package com.raite.studyroom.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Delete
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
import com.raite.studyroom.domain.model.Task
import com.raite.studyroom.domain.model.TaskPriority
import com.raite.studyroom.util.toShortDate

@Composable
fun TaskRow(
    task: Task,
    onToggle: (Boolean) -> Unit,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier.fillMaxWidth().padding(horizontal = 8.dp, vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Checkbox(checked = task.completed, onCheckedChange = onToggle)
        Column(Modifier.weight(1f)) {
            Text(
                task.title,
                style = MaterialTheme.typography.bodyLarge,
                textDecoration = if (task.completed) TextDecoration.LineThrough else null,
            )
            val meta = buildList {
                add(task.priority.label())
                task.dueAt?.let { add("Due ${it.toShortDate()}") }
            }.joinToString(" · ")
            Text(
                meta,
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            if (task.syncState != com.raite.studyroom.domain.model.SyncState.SYNCED) {
                SyncIndicator(task.syncState, Modifier.padding(top = 2.dp))
            }
        }
        IconButton(onClick = onDelete) {
            Icon(Icons.Rounded.Delete, contentDescription = "Delete task")
        }
    }
}

fun TaskPriority.label(): String = when (this) {
    TaskPriority.LOW -> "Low"
    TaskPriority.MEDIUM -> "Medium"
    TaskPriority.HIGH -> "High"
}

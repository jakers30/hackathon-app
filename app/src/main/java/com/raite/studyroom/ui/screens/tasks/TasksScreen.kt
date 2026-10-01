package com.raite.studyroom.ui.screens.tasks

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
import androidx.compose.material.icons.rounded.Checklist
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.raite.studyroom.domain.model.TaskPriority
import com.raite.studyroom.ui.components.CtaButton
import com.raite.studyroom.ui.components.EmptyState
import com.raite.studyroom.ui.components.MainScaffold
import com.raite.studyroom.ui.components.OfflineBanner
import com.raite.studyroom.ui.components.TaskRow
import com.raite.studyroom.ui.components.label
import com.raite.studyroom.ui.navigation.Routes

@Composable
fun TasksScreen(
    viewModel: TasksViewModel,
    onNavigate: (String) -> Unit,
) {
    val tasks by viewModel.taskList.collectAsStateWithLifecycle()

    MainScaffold(
        currentRoute = Routes.TASKS,
        onNavigate = onNavigate,
        topBar = {
            Column {
                OfflineBanner()
                Text(
                    "My Tasks",
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
            item {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = viewModel.title,
                        onValueChange = viewModel::onTitleChange,
                        label = { Text("What do you need to do?") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                    )
                    OutlinedTextField(
                        value = viewModel.dueDate,
                        onValueChange = viewModel::onDueDateChange,
                        label = { Text("Deadline (YYYY-MM-DD, optional)") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                    )
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        TaskPriority.entries.forEach { option ->
                            FilterChip(
                                selected = viewModel.priority == option,
                                onClick = { viewModel.onPriorityChange(option) },
                                label = { Text(option.label()) },
                            )
                        }
                    }
                    CtaButton(
                        text = "Add task",
                        icon = Icons.Rounded.Add,
                        onClick = viewModel::add,
                        modifier = Modifier.fillMaxWidth(),
                    )
                }
            }

            if (tasks.isEmpty()) {
                item {
                    EmptyState(
                        icon = Icons.Rounded.Checklist,
                        message = "No tasks yet. Add one above.",
                    )
                }
            } else {
                items(tasks, key = { it.id }) { task ->
                    TaskRow(
                        task = task,
                        onToggle = { viewModel.toggle(task, it) },
                        onDelete = { viewModel.delete(task) },
                    )
                }
            }
        }
    }
}

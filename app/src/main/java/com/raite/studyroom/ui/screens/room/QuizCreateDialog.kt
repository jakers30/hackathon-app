package com.raite.studyroom.ui.screens.room

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.unit.dp

private val questionTypes = listOf("multiple_choice", "true_false", "identification", "short_answer")

/** Host options for quiz generation (spec section 3.6). */
@Composable
fun QuizCreateDialog(
    onDismiss: () -> Unit,
    onCreate: (String, List<String>, Int) -> Unit,
) {
    var type by remember { mutableStateOf("quiz") }
    var count by remember { mutableStateOf("5") }
    val selected = remember { mutableStateOf(questionTypes.toSet()) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Generate a quiz") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    listOf("quiz", "exam").forEach { option ->
                        FilterChip(
                            selected = type == option,
                            onClick = { type = option },
                            label = { Text(option.replaceFirstChar { it.uppercase() }) },
                        )
                    }
                }
                Text("Question types", style = MaterialTheme.typography.titleMedium)
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    questionTypes.forEach { option ->
                        FilterChip(
                            selected = option in selected.value,
                            onClick = {
                                selected.value = if (option in selected.value) {
                                    selected.value - option
                                } else {
                                    selected.value + option
                                }
                            },
                            label = { Text(option.replace('_', ' ')) },
                        )
                    }
                }
                OutlinedTextField(
                    value = count,
                    onValueChange = { count = it.filter { c -> c.isDigit() } },
                    label = { Text("Number of questions") },
                    singleLine = true,
                )
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    val n = count.toIntOrNull() ?: 5
                    onCreate(type, selected.value.toList(), n)
                },
                enabled = selected.value.isNotEmpty(),
            ) { Text("Generate") }
        },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Cancel") } },
    )
}

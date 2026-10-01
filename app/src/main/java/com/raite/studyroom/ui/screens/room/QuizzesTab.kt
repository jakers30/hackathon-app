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
import androidx.compose.material.icons.rounded.Quiz
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.raite.studyroom.domain.model.Quiz
import com.raite.studyroom.domain.model.QuizStatus
import com.raite.studyroom.ui.components.CtaButton
import com.raite.studyroom.ui.components.EmptyState

@Composable
fun QuizzesTab(
    quizzes: List<Quiz>,
    isHost: Boolean,
    online: Boolean,
    onCreateQuiz: (String, List<String>, Int) -> Unit,
    onOpenQuiz: (String) -> Unit,
) {
    var showDialog by remember { mutableStateOf(false) }

    LazyColumn(
        Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        if (isHost) {
            item {
                CtaButton(
                    text = "Generate Quiz",
                    onClick = { showDialog = true },
                    enabled = online,
                    modifier = Modifier.fillMaxWidth(),
                )
            }
            item {
                Text(
                    "Generated quizzes are drafts. Review and approve before students see them.",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }
        if (quizzes.isEmpty()) {
            item {
                EmptyState(
                    icon = Icons.Rounded.Quiz,
                    message = if (isHost) "No quizzes yet. Generate one above."
                    else "No published quizzes yet.",
                )
            }
        } else {
            items(quizzes, key = { it.id }) { quiz ->
                QuizCard(quiz = quiz, showAnswers = isHost, onClick = { onOpenQuiz(quiz.id) })
            }
        }
    }

    if (showDialog) {
        QuizCreateDialog(
            onDismiss = { showDialog = false },
            onCreate = { type, types, count ->
                showDialog = false
                onCreateQuiz(type, types, count)
            },
        )
    }
}

@Composable
private fun QuizCard(quiz: Quiz, showAnswers: Boolean, onClick: () -> Unit) {
    Card(Modifier.fillMaxWidth()) {
        Column(Modifier.padding(12.dp)) {
            Text(quiz.title, style = MaterialTheme.typography.titleMedium)
            Text(
                "${quiz.type} · ${quiz.questionCount} questions",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            Row(Modifier.padding(top = 4.dp), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                AssistChip(
                    onClick = {},
                    label = { Text(if (quiz.status == QuizStatus.PUBLISHED) "Published" else "Draft") },
                )
                if (showAnswers) {
                    TextButton(onClick = onClick) {
                        Text(if (quiz.status == QuizStatus.PUBLISHED) "View" else "Review & publish")
                    }
                }
            }
        }
    }
}

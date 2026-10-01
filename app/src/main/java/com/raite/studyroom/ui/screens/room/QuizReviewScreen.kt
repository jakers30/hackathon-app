package com.raite.studyroom.ui.screens.room

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Visibility
import androidx.compose.material.icons.rounded.VisibilityOff
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.raite.studyroom.domain.model.QuizQuestion
import com.raite.studyroom.ui.components.AiGeneratedLabel
import com.raite.studyroom.ui.components.CtaButton

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuizReviewScreen(
    viewModel: QuizReviewViewModel,
    onBack: () -> Unit,
    onPublished: () -> Unit,
) {
    val quiz by viewModel.quiz.collectAsStateWithLifecycle()
    val questions by viewModel.questions.collectAsStateWithLifecycle()
    val published = quiz?.status?.name == "PUBLISHED"

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(quiz?.title ?: "Quiz review") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Rounded.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = viewModel::toggleAnswers) {
                        Icon(
                            if (viewModel.showAnswers) Icons.Rounded.VisibilityOff else Icons.Rounded.Visibility,
                            contentDescription = "Preview as a student",
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimary,
                    actionIconContentColor = MaterialTheme.colorScheme.onPrimary,
                ),
            )
        },
        bottomBar = {
            if (!published) {
                Column(Modifier.padding(12.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Checkbox(
                            checked = viewModel.confirmed,
                            onCheckedChange = { viewModel.updateConfirmed(it) },
                        )
                        Text(
                            "I have verified the questions and answer key.",
                            style = MaterialTheme.typography.bodyMedium,
                        )
                    }
                    viewModel.message?.let {
                        Text(it, color = MaterialTheme.colorScheme.error)
                    }
                    CtaButton(
                        text = "Approve and Publish",
                        onClick = { viewModel.publish(onPublished) },
                        enabled = viewModel.confirmed,
                        modifier = Modifier.fillMaxWidth(),
                    )
                }
            }
        },
    ) { innerPadding ->
        LazyColumn(
            Modifier.fillMaxSize().padding(innerPadding).padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            item { AiGeneratedLabel() }
            itemsIndexed(questions) { index, question ->
                QuestionCard(index + 1, question, viewModel.showAnswers)
            }
        }
    }
}

@Composable
private fun QuestionCard(number: Int, question: QuizQuestion, showAnswers: Boolean) {
    Card(Modifier.fillMaxWidth()) {
        Column(Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Text("$number. ${question.prompt}", style = MaterialTheme.typography.titleMedium)
            question.options.forEach { option ->
                Text("• $option", style = MaterialTheme.typography.bodyMedium)
            }
            if (showAnswers) {
                Text(
                    "Answer: ${question.correctAnswer}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.primary,
                )
                if (question.explanation.isNotBlank()) {
                    Text(
                        question.explanation,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
                question.sourceResourceName?.let {
                    Text(
                        "Source: $it",
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            }
        }
    }
}

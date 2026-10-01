package com.raite.studyroom.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ExpandLess
import androidx.compose.material.icons.rounded.ExpandMore
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.raite.studyroom.domain.model.ReviewerSection

/** Collapsible reviewer section (spec section 3.4B: sections, not one text block). */
@Composable
fun ReviewerSection(section: ReviewerSection, modifier: Modifier = Modifier) {
    var expanded by remember { mutableStateOf(true) }
    Card(modifier.fillMaxWidth()) {
        Column(Modifier.padding(12.dp)) {
            Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                Text(section.title, style = MaterialTheme.typography.titleMedium, modifier = Modifier.weight(1f))
                IconButton(onClick = { expanded = !expanded }) {
                    Icon(
                        if (expanded) Icons.Rounded.ExpandLess else Icons.Rounded.ExpandMore,
                        contentDescription = if (expanded) "Collapse" else "Expand",
                    )
                }
            }
            AnimatedVisibility(expanded) {
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    section.content.forEach { line ->
                        Text("• $line", style = MaterialTheme.typography.bodyMedium)
                    }
                }
            }
        }
    }
}

/** The bottom AI prompt bar (spec section 3.4C). */
@Composable
fun AiPromptBar(
    prompt: String,
    onPromptChange: (String) -> Unit,
    onGenerate: () -> Unit,
    enabled: Boolean,
    busy: Boolean,
    offline: Boolean,
    modifier: Modifier = Modifier,
) {
    Card(modifier.fillMaxWidth()) {
        Column(Modifier.padding(12.dp)) {
            Text("AI Study Assistant", style = MaterialTheme.typography.titleMedium)
            OutlinedTextField(
                value = prompt,
                onValueChange = onPromptChange,
                label = { Text("Ask AI to modify your study materials…") },
                enabled = enabled,
                modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
            )
            Row(Modifier.padding(top = 8.dp), verticalAlignment = Alignment.CenterVertically) {
                Text(
                    if (offline) "Connect to the internet to use AI." else "",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.weight(1f),
                )
                CtaButton(
                    text = if (busy) "Working…" else "Generate",
                    onClick = onGenerate,
                    enabled = enabled && !busy && prompt.isNotBlank(),
                )
            }
        }
    }
}

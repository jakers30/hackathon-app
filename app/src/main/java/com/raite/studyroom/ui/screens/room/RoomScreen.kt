package com.raite.studyroom.ui.screens.room

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Group
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.raite.studyroom.ui.components.AiPromptBar
import com.raite.studyroom.ui.components.OfflineBanner
import com.raite.studyroom.ui.components.rememberIsOnline
import com.raite.studyroom.util.queryFileMeta

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RoomScreen(
    viewModel: RoomViewModel,
    onBack: () -> Unit,
    onOpenQuiz: (String) -> Unit,
) {
    val room by viewModel.room.collectAsStateWithLifecycle()
    val members by viewModel.members.collectAsStateWithLifecycle()
    val resources by viewModel.resources.collectAsStateWithLifecycle()
    val roadmap by viewModel.roadmap.collectAsStateWithLifecycle()
    val reviewer by viewModel.reviewer.collectAsStateWithLifecycle()
    val quizzes by viewModel.quizzes.collectAsStateWithLifecycle()

    val online = rememberIsOnline()
    val context = LocalContext.current
    var tab by remember { mutableIntStateOf(0) }
    var prompt by remember { mutableStateOf("") }

    val picker = rememberLauncherForActivityResult(ActivityResultContracts.OpenDocument()) { uri ->
        if (uri != null) viewModel.upload(uri, queryFileMeta(context, uri))
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(room?.name ?: "Study Room") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Rounded.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    Icon(Icons.Rounded.Group, contentDescription = "Members")
                    Text("${members.size}", Modifier.padding(start = 4.dp, end = 12.dp))
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimary,
                    actionIconContentColor = MaterialTheme.colorScheme.onPrimary,
                ),
            )
        },
        floatingActionButton = {
            if (tab == 0 && viewModel.isHost) {
                FloatingActionButton(
                    onClick = {
                        picker.launch(
                            arrayOf(
                                "application/pdf",
                                "image/*",
                                "text/plain",
                                "application/msword",
                                "application/vnd.openxmlformats-officedocument.wordprocessingml.document",
                            )
                        )
                    },
                    containerColor = MaterialTheme.colorScheme.tertiary,
                    contentColor = MaterialTheme.colorScheme.onTertiary,
                ) {
                    Icon(Icons.Rounded.Add, contentDescription = "Upload resource")
                }
            }
        },
        bottomBar = {
            if (tab == 1) {
                AiPromptBar(
                    prompt = prompt,
                    onPromptChange = { prompt = it },
                    onGenerate = {
                        if (prompt.isNotBlank()) {
                            viewModel.modify(prompt)
                            prompt = ""
                        }
                    },
                    enabled = online,
                    busy = viewModel.busy,
                    offline = !online,
                    modifier = Modifier.padding(8.dp),
                )
            }
        },
    ) { innerPadding ->
        Column(Modifier.fillMaxSize().padding(innerPadding)) {
            OfflineBanner(online)
            TabRow(selectedTabIndex = tab) {
                listOf("Resources", "Materials", "Quizzes").forEachIndexed { index, title ->
                    Tab(selected = tab == index, onClick = { tab = index }, text = { Text(title) })
                }
            }
            viewModel.message?.let {
                Text(
                    it,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                )
            }
            if (viewModel.busy) {
                CircularProgressIndicator(Modifier.padding(16.dp))
            }
            when (tab) {
                0 -> ResourcesTab(
                    resources = resources,
                    isHost = viewModel.isHost,
                    onDownload = viewModel::download,
                    onDelete = viewModel::deleteResource,
                )
                1 -> MaterialsTab(
                    roadmap = roadmap,
                    reviewer = reviewer,
                    online = online,
                    busy = viewModel.busy,
                    onToggle = viewModel::toggleRoadmap,
                    onGenerateRoadmap = viewModel::generateRoadmap,
                    onGenerateReviewer = viewModel::generateReviewer,
                )
                else -> QuizzesTab(
                    quizzes = quizzes,
                    isHost = viewModel.isHost,
                    online = online,
                    onCreateQuiz = { type, types, count ->
                        viewModel.generateQuiz(type, types, count, onOpenQuiz)
                    },
                    onOpenQuiz = onOpenQuiz,
                )
            }
        }
    }
}

package com.raite.studyroom.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.raite.studyroom.ui.SessionViewModel
import com.raite.studyroom.ui.screens.auth.AuthScreen
import com.raite.studyroom.ui.screens.auth.AuthViewModel
import com.raite.studyroom.ui.screens.dashboard.DashboardScreen
import com.raite.studyroom.ui.screens.dashboard.DashboardViewModel
import com.raite.studyroom.ui.screens.intro.IntroScreen
import com.raite.studyroom.ui.screens.room.QuizReviewScreen
import com.raite.studyroom.ui.screens.room.QuizReviewViewModel
import com.raite.studyroom.ui.screens.room.RoomScreen
import com.raite.studyroom.ui.screens.room.RoomViewModel
import com.raite.studyroom.ui.screens.rooms.CreateRoomScreen
import com.raite.studyroom.ui.screens.rooms.CreateRoomViewModel
import com.raite.studyroom.ui.screens.rooms.RoomsScreen
import com.raite.studyroom.ui.screens.rooms.RoomsViewModel
import com.raite.studyroom.ui.screens.settings.SettingsScreen
import com.raite.studyroom.ui.screens.settings.SettingsViewModel
import com.raite.studyroom.ui.screens.tasks.TasksScreen
import com.raite.studyroom.ui.screens.tasks.TasksViewModel

@Composable
fun AppNavHost(
    navController: NavHostController = rememberNavController(),
) {
    val sessionViewModel: SessionViewModel = hiltViewModel()
    val loggedIn by sessionViewModel.isLoggedIn.collectAsStateWithLifecycle()

    NavHost(navController = navController, startDestination = Routes.INTRO) {

        composable(Routes.INTRO) {
            IntroScreen(
                onFinished = {
                    val target = if (loggedIn == true) Routes.DASHBOARD else Routes.AUTH
                    navController.navigate(target) {
                        popUpTo(Routes.INTRO) { inclusive = true }
                    }
                }
            )
        }

        composable(Routes.AUTH) {
            val viewModel: AuthViewModel = hiltViewModel()
            AuthScreen(
                viewModel = viewModel,
                onAuthenticated = {
                    navController.navigate(Routes.DASHBOARD) {
                        popUpTo(Routes.AUTH) { inclusive = true }
                    }
                },
            )
        }

        composable(Routes.DASHBOARD) {
            val viewModel: DashboardViewModel = hiltViewModel()
            DashboardScreen(
                viewModel = viewModel,
                onNavigate = { navigateTopLevel(navController, it) },
                onOpenRoom = { navController.navigate(Routes.room(it)) },
            )
        }

        composable(Routes.ROOMS) {
            val viewModel: RoomsViewModel = hiltViewModel()
            RoomsScreen(
                viewModel = viewModel,
                onNavigate = { navigateTopLevel(navController, it) },
                onOpenRoom = { navController.navigate(Routes.room(it)) },
            )
        }

        composable(Routes.TASKS) {
            val viewModel: TasksViewModel = hiltViewModel()
            TasksScreen(
                viewModel = viewModel,
                onNavigate = { navigateTopLevel(navController, it) },
            )
        }

        composable(Routes.SETTINGS) {
            val viewModel: SettingsViewModel = hiltViewModel()
            SettingsScreen(
                viewModel = viewModel,
                onNavigate = { navigateTopLevel(navController, it) },
                onSignedOut = {
                    navController.navigate(Routes.AUTH) {
                        popUpTo(0) { inclusive = true }
                    }
                },
            )
        }

        composable(Routes.CREATE_ROOM) {
            val viewModel: CreateRoomViewModel = hiltViewModel()
            CreateRoomScreen(
                viewModel = viewModel,
                onBack = { navController.popBackStack() },
                onCreated = { roomId ->
                    navController.navigate(Routes.room(roomId)) {
                        popUpTo(Routes.CREATE_ROOM) { inclusive = true }
                    }
                },
            )
        }

        composable(
            route = Routes.ROOM,
            arguments = listOf(navArgument(Routes.ROOM_ARG) { type = NavType.StringType }),
        ) {
            val viewModel: RoomViewModel = hiltViewModel()
            RoomScreen(
                viewModel = viewModel,
                onBack = { navController.popBackStack() },
                onOpenQuiz = { navController.navigate(Routes.quiz(it)) },
            )
        }

        composable(
            route = Routes.QUIZ,
            arguments = listOf(navArgument(Routes.QUIZ_ARG) { type = NavType.StringType }),
        ) {
            val viewModel: QuizReviewViewModel = hiltViewModel()
            QuizReviewScreen(
                viewModel = viewModel,
                onBack = { navController.popBackStack() },
                onPublished = { navController.popBackStack() },
            )
        }
    }
}

/** Bottom-nav navigation: single instance, state is restored on return. */
private fun navigateTopLevel(navController: NavHostController, route: String) {
    navController.navigate(route) {
        popUpTo(Routes.DASHBOARD) { saveState = true }
        launchSingleTop = true
        restoreState = true
    }
}

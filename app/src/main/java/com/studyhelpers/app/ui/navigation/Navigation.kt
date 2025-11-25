package com.studyhelpers.app.ui.navigation

import com.studyhelpers.app.ui.focustimer.FocusTimerScreen
import androidx.navigation.compose.composable
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.studyhelpers.app.ui.addtask.AddTaskScreen
import com.studyhelpers.app.ui.home.HomeScreen
import com.studyhelpers.app.ui.home.HomeViewModel
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.studyhelpers.app.ui.edittask.EditTaskScreen
@Composable
fun AppNavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Screen.Home.route
    ) {
        composable(Screen.Home.route) {
            val vm: HomeViewModel = hiltViewModel()
            HomeScreen(
                viewModel = vm,
                onAddTaskClick = { navController.navigate(Screen.AddTask.route) },
                onTaskClick = { id -> navController.navigate(Screen.EditTask.createRoute(id)) },
                onFocusTimerClick = { navController.navigate(Screen.FocusTimer.route) }
            )
        }
        composable(Screen.FocusTimer.route) {
            FocusTimerScreen(
                onBack = { navController.popBackStack() }
            )
        }
        composable(Screen.AddTask.route) {
            AddTaskScreen(
                onBack = { navController.popBackStack() }
            )
        }
        composable(
            route = Screen.EditTask.route,
            arguments = listOf(navArgument("taskId") { type = NavType.IntType })
        ) {
            EditTaskScreen(
                onBack = { navController.popBackStack() }
            )
        }
    }
}
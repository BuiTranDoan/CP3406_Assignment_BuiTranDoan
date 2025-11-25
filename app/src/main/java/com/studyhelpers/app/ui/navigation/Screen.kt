package com.studyhelpers.app.ui.navigation

sealed class Screen(val route: String) {
    object Login : Screen("login")
    object Home : Screen("home")
    object AddTask : Screen("addTask")
    object EditTask : Screen("editTask/{taskId}") {
        fun createRoute(id: Int) = "editTask/$id"
    }
    object Calendar : Screen("calendar")
    object FocusTimer : Screen("focus_timer")
    object Profile : Screen("profile")
}
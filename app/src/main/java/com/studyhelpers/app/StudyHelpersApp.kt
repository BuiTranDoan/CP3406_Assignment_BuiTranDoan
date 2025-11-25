package com.studyhelpers.app

import androidx.compose.runtime.collectAsState
import com.studyhelpers.app.ui.calendar.CalendarScreen
import com.studyhelpers.app.ui.calendar.CalendarViewModel
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.studyhelpers.app.ui.addtask.AddTaskScreen
import com.studyhelpers.app.ui.auth.AuthViewModel
import com.studyhelpers.app.ui.auth.LoginScreen
import com.studyhelpers.app.ui.edittask.EditTaskScreen
import com.studyhelpers.app.ui.focustimer.FocusTimerScreen
import com.studyhelpers.app.ui.home.HomeScreen
import com.studyhelpers.app.ui.home.HomeViewModel
import com.studyhelpers.app.ui.navigation.Screen
import com.studyhelpers.app.ui.profile.ProfileScreen

@Composable
fun StudyHelpersApp() {
    val navController = rememberNavController()
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = backStackEntry?.destination
    val authViewModel: AuthViewModel = hiltViewModel()
    val authState by authViewModel.uiState.collectAsState()

    val bottomDestinations = listOf(
        Screen.Home,
        Screen.Calendar,
        Screen.FocusTimer,
        Screen.Profile
    )

    val showBottomBar = currentDestination?.route in bottomDestinations.map { it.route }

    Scaffold(
        bottomBar = {
            if (showBottomBar) {
                NavigationBar {
                    bottomDestinations.forEach { screen ->
                        NavigationBarItem(
                            selected = currentDestination.isBottomDest(screen.route),
                            onClick = {
                                navController.navigate(screen.route) {
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            },
                            icon = {
                                when (screen) {
                                    Screen.Home ->
                                        Icon(Icons.Filled.Home, contentDescription = "Home")
                                    Screen.Calendar ->
                                        Icon(Icons.Filled.CalendarMonth, contentDescription = "Calendar")
                                    Screen.FocusTimer ->
                                        Icon(Icons.Filled.Timer, contentDescription = "Focus")
                                    Screen.Profile ->
                                        Icon(Icons.Filled.Person, contentDescription = "Profile")
                                    else -> {}
                                }
                            },
                            label = {
                                Text(
                                    when (screen) {
                                        Screen.Home -> "Home"
                                        Screen.Calendar -> "Calendar"
                                        Screen.FocusTimer -> "Focus"
                                        Screen.Profile -> "Profile"
                                        else -> ""
                                    }
                                )
                            }
                        )
                    }
                }
            }
        }
    ) { innerPadding ->

        NavHost(
            navController = navController,
            // if already logged in, go straight to Home
            startDestination = if (authState.isLoggedIn) {
                Screen.Home.route
            } else {
                Screen.Login.route
            },
            modifier = Modifier.padding(innerPadding)
        ) {

            composable(Screen.Login.route) {
                LoginScreen(
                    onLoginSuccess = {
                        navController.navigate(Screen.Home.route) {
                            popUpTo(Screen.Login.route) { inclusive = true }
                        }
                    },
                    viewModel = authViewModel
                )
            }

            composable(Screen.Home.route) {
                val vm: HomeViewModel = hiltViewModel()
                val currentUserName =
                    authState.currentUser?.displayName ?: authState.currentUser?.username

                HomeScreen(
                    viewModel = vm,
                    onAddTaskClick = { navController.navigate(Screen.AddTask.route) },
                    onTaskClick = { id ->
                        navController.navigate(Screen.EditTask.createRoute(id))
                    },
                    onFocusTimerClick = {
                        navController.navigate(Screen.FocusTimer.route)
                    },
                    currentUserName = currentUserName
                )
            }

            composable(Screen.AddTask.route) {
                AddTaskScreen(
                    onBack = { navController.popBackStack() }
                )
            }

            composable(Screen.EditTask.route) {
                EditTaskScreen(
                    onBack = { navController.popBackStack() }
                )
            }

            composable(Screen.FocusTimer.route) {
                FocusTimerScreen(
                    onBack = { navController.popBackStack() }
                )
            }

            composable(Screen.Calendar.route) {
                val vm: CalendarViewModel = hiltViewModel()
                CalendarScreen(
                    viewModel = vm,
                    onTaskClick = { id ->
                        navController.navigate(Screen.EditTask.createRoute(id))
                    }
                )
            }


            composable(Screen.Profile.route) {
                ProfileScreen(
                    uiState = authState,
                    onLogout = {
                        authViewModel.logout()
                        navController.navigate(Screen.Login.route) {
                            popUpTo(navController.graph.findStartDestination().id) {
                                inclusive = true
                            }
                        }
                    }
                )
            }
        }
    }
}

private fun NavDestination?.isBottomDest(route: String): Boolean {
    return this?.hierarchy?.any { it.route == route } == true
}
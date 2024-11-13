package com.codewithnathan.taskly.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.codewithnathan.taskly.ui.screens.TaskFormScreen
import com.codewithnathan.taskly.ui.screens.TaskFormDestination
import com.codewithnathan.taskly.ui.screens.TaskListDestination
import com.codewithnathan.taskly.ui.screens.TaskListScreen

@Composable
fun NavigationHost(
    navController: NavHostController
) {
    NavHost(
        navController = navController,
        startDestination = TaskListDestination.route,
    ) {
        composable(route = TaskListDestination.route) {
            TaskListScreen(
                navigateToTaskForm = {
                    navController.navigate(TaskFormDestination.route)
                }
            )
        }
        composable(route = TaskFormDestination.route) {
            TaskFormScreen(
                navigateUp = { navController.navigateUp() }
            )
        }
    }
}
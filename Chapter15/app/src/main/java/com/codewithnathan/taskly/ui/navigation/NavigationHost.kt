package com.codewithnathan.taskly.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
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
                    navController.navigate("${TaskFormDestination.route}/0")
                },
                navigateToTaskDetail = {
                    navController.navigate("${TaskFormDestination.route}/${it}")
                }
            )
        }
        composable(
            route = TaskFormDestination.routeWithArgs,
            arguments = listOf(
                navArgument(TaskFormDestination.TASK_ID) {
                    type = NavType.IntType
                }
            )
        ) {
            TaskFormScreen(
                navigateUp = { navController.navigateUp() }
            )
        }
    }
}
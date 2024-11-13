package com.codewithnathan.taskly

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.codewithnathan.taskly.ui.navigation.NavigationHost
import com.codewithnathan.taskly.ui.theme.TasklyTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TasklyTheme {
                CreateNav()
            }
        }
    }
}

@Composable
fun CreateNav(navController: NavHostController = rememberNavController()) {
    NavigationHost(navController = navController)
}

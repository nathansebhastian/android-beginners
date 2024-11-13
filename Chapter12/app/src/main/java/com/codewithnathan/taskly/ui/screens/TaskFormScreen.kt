package com.codewithnathan.taskly.ui.screens

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.codewithnathan.taskly.R
import com.codewithnathan.taskly.ui.navigation.NavigationDestination

object TaskFormDestination : NavigationDestination {
    override val route = "form"
    override val titleRes = R.string.task_form_title
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskFormScreen(
    navigateUp: () -> Unit
) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(title = { Text(stringResource(TaskFormDestination.titleRes)) },
                navigationIcon = {
                    IconButton(onClick = navigateUp) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.back_button)
                        )
                    }
                })
        },
    ) { innerPadding ->
        Text(
            text = "Task Form Screen", modifier = Modifier.padding(innerPadding)
        )
    }
}
package com.codewithnathan.taskly.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.codewithnathan.taskly.R
import com.codewithnathan.taskly.data.Task
import com.codewithnathan.taskly.ui.ViewModelProvider
import com.codewithnathan.taskly.ui.navigation.NavigationDestination
import com.codewithnathan.taskly.ui.viewmodels.TaskListViewModel

object TaskListDestination : NavigationDestination {
    override val route = "home"
    override val titleRes = R.string.task_list_title
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskListScreen(
    navigateToTaskForm: () -> Unit,
    viewModel: TaskListViewModel = viewModel(factory = ViewModelProvider.Factory)
) {
    val taskListState by viewModel.taskListUiState.collectAsState()
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(stringResource(TaskListDestination.titleRes)) }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = navigateToTaskForm,
                shape = MaterialTheme.shapes.medium,
                modifier = Modifier
                    .padding(16.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = stringResource(R.string.create_new_task)
                )
            }
        }
    ) { innerPadding ->
        TaskList(
            taskList = taskListState.taskList,
            modifier = Modifier.padding(innerPadding)
        )
    }
}

@Composable
fun TaskList(
    taskList: List<Task>,
    modifier: Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        if (taskList.isEmpty()) {
            Text(
                text = stringResource(R.string.no_task_description),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.titleLarge,
            )
        } else {
            LazyColumn {
                items(items = taskList, key = { it.id }) { task ->
                    SingleTask(
                        task = task
                    )
                }
            }
        }
    }
}

@Composable
fun SingleTask(
    task: Task
) {
    Card(
        modifier = Modifier.padding(dimensionResource(id = R.dimen.padding_small)), // 8dp
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(dimensionResource(id = R.dimen.padding_large)) // 20dp
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.padding_small))
        ) {
            Text(
                text = task.title,
                style = MaterialTheme.typography.titleLarge,
            )
            Spacer(Modifier.weight(1f))
            Text(
                text = task.notes,
                style = MaterialTheme.typography.titleMedium
            )
        }
    }
}

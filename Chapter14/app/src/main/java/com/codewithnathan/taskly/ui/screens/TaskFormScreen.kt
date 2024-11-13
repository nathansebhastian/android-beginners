package com.codewithnathan.taskly.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.codewithnathan.taskly.R
import com.codewithnathan.taskly.data.Task
import com.codewithnathan.taskly.ui.ViewModelProvider
import com.codewithnathan.taskly.ui.navigation.NavigationDestination
import com.codewithnathan.taskly.ui.viewmodels.TaskFormViewModel
import kotlinx.coroutines.launch

object TaskFormDestination : NavigationDestination {
    override val route = "form"
    override val titleRes = R.string.task_form_title
    const val TASK_ID = "taskId"
    val routeWithArgs = "$route/{$TASK_ID}"
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskFormScreen(
    navigateUp: () -> Unit,
    viewModel: TaskFormViewModel = viewModel(factory = ViewModelProvider.Factory)
) {
    val coroutineScope = rememberCoroutineScope()
    val taskFormUiState = viewModel.taskFormUiState
    val isNewTask = viewModel.isNewTask()
    var showDeleteConfirmation by remember { mutableStateOf(false) }

    val onSaveClick: () -> Unit = {
        coroutineScope.launch {
            if(isNewTask){
                viewModel.saveTask()
            } else {
                viewModel.updateTask()
            }
            navigateUp()
        }
    }

    val onDeleteClick: () -> Unit = {
        coroutineScope.launch {
            viewModel.deleteTask()
            navigateUp()
        }
    }

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
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(dimensionResource(id = R.dimen.padding_medium)),
            verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.padding_large))
        ) {
            TaskInputForm(
                taskDetails = taskFormUiState.task,
                onValueChange = viewModel::updateUiState,
                modifier = Modifier.fillMaxWidth()
            )
            if (!isNewTask) {
                Button(
                    onClick = { showDeleteConfirmation = true },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Red,        // Background color
                        contentColor = Color.White         // Text color
                    ),
                ) {
                    Text(stringResource(R.string.delete_action))
                }
            }
            Button(
                onClick = onSaveClick,
                enabled = taskFormUiState.isEntryValid,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(stringResource(R.string.save_task))
            }
        }
        if (showDeleteConfirmation) {
            AlertDialog(
                onDismissRequest = { showDeleteConfirmation = false }, // Close dialog when clicking outside
                title = { Text("Confirm Delete") },
                text = { Text("Are you sure you want to delete this task?") },
                confirmButton = {
                    TextButton(
                        onClick = {
                            showDeleteConfirmation = false
                            onDeleteClick()
                        }
                    ) {
                        Text("Confirm", color = Color.Red)
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showDeleteConfirmation = false }) {
                        Text("Cancel")
                    }
                }
            )
        }
    }
}

@Composable
fun TaskInputForm(
    taskDetails: Task,
    modifier: Modifier = Modifier,
    onValueChange: (Task) -> Unit = {},
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.padding_medium)) // 16dp
    ) {
        OutlinedTextField(
            value = taskDetails.title,
            onValueChange = { onValueChange(taskDetails.copy(title = it)) },
            label = { Text(stringResource(R.string.task_title_req)) },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )
        OutlinedTextField(
            value = taskDetails.notes,
            onValueChange = { onValueChange(taskDetails.copy(notes = it)) },
            label = { Text(stringResource(R.string.task_notes)) },
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 100.dp),  // Adjust height for multiline input
            singleLine = false,  // Allows multiple lines
            maxLines = 5
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = stringResource(R.string.task_is_completed))
            Spacer(modifier = Modifier.weight(1f)) // To push the switch to the end
            Switch(
                checked = taskDetails.isCompleted,
                onCheckedChange = { onValueChange(taskDetails.copy(isCompleted = it)) },
            )
        }
        Text(
            text = stringResource(R.string.required_fields), // *required field
            modifier = Modifier.padding(start = dimensionResource(id = R.dimen.padding_medium))
        )
    }
}

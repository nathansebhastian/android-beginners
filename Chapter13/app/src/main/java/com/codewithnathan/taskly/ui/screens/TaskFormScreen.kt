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
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskFormScreen(
    navigateUp: () -> Unit,
    viewModel: TaskFormViewModel = viewModel(factory = ViewModelProvider.Factory)
) {
    val coroutineScope = rememberCoroutineScope()
    val taskFormUiState = viewModel.taskFormUiState

    val onSaveClick: () -> Unit = {
        coroutineScope.launch {
            viewModel.saveTask()
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
            Button(
                onClick = onSaveClick,
                enabled = taskFormUiState.isEntryValid,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(stringResource(R.string.save_task))
            }
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

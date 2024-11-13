package com.codewithnathan.taskly.ui.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.codewithnathan.taskly.data.Task
import com.codewithnathan.taskly.data.TaskDao

class TaskFormViewModel(
    private val taskDao: TaskDao
) : ViewModel()
{
    var taskFormUiState by mutableStateOf(TaskFormUiState())

    fun updateUiState(task: Task) {
        taskFormUiState =
            TaskFormUiState(task = task, isEntryValid = validateInput(task))
    }

    suspend fun saveTask() {
        if (validateInput()) {
            taskDao.insert(taskFormUiState.task)
        }
    }

    private fun validateInput(task: Task = taskFormUiState.task): Boolean {
        return task.title.isNotBlank()
    }
}

data class TaskFormUiState(
    val task: Task = Task(
        id = 0,
        title = "",
        notes = "",
        isCompleted = false
    ),
    val isEntryValid: Boolean = false
)
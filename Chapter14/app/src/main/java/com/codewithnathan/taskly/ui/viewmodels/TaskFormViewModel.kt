package com.codewithnathan.taskly.ui.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.codewithnathan.taskly.data.Task
import com.codewithnathan.taskly.data.TaskDao
import com.codewithnathan.taskly.ui.screens.TaskFormDestination
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class TaskFormViewModel(
    savedStateHandle: SavedStateHandle,
    private val taskDao: TaskDao
) : ViewModel()
{
    var taskFormUiState by mutableStateOf(TaskFormUiState())

    private val taskId: Int = savedStateHandle[TaskFormDestination.TASK_ID] ?: 0

    init {
        // Launch coroutine to fetch task data when taskId is not null
        if (taskId != 0) {
            viewModelScope.launch {
                taskFormUiState = taskDao.getTask(taskId)
                    .filterNotNull()
                    .first() // Fetches the first available non-null task
                    .toTaskFormUiState(true) // Converts the task to TaskFormUiState
            }
        }
    }

    fun updateUiState(task: Task) {
        taskFormUiState =
            TaskFormUiState(task = task, isEntryValid = validateInput(task))
    }

    suspend fun saveTask() {
        if (validateInput()) {
            taskDao.insert(taskFormUiState.task)
        }
    }

    suspend fun updateTask() {
        if (validateInput()) {
            taskDao.update(taskFormUiState.task)
        }
    }

    fun isNewTask(): Boolean {
        return taskFormUiState.task.id == 0
    }

    suspend fun deleteTask() {
        taskDao.delete(taskFormUiState.taskDetails.toTask())
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

/**
 * Extension function to convert [Task] to [TaskFormUiState]
 */
fun Task.toTaskFormUiState(isEntryValid: Boolean = false): TaskFormUiState = TaskFormUiState(
    task = this,
    isEntryValid = isEntryValid
)
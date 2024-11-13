package com.codewithnathan.taskly.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.codewithnathan.taskly.data.Task
import com.codewithnathan.taskly.data.TaskDao
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class TaskListViewModel(taskDao: TaskDao) : ViewModel() {

    val taskListUiState: StateFlow<TaskListUiState> = taskDao.getAllTasks()
            .map { TaskListUiState(it) }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000L),
                initialValue = TaskListUiState()
            )

}

data class TaskListUiState(val taskList: List<Task> = listOf())
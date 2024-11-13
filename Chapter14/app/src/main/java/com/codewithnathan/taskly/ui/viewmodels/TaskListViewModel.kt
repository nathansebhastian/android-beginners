package com.codewithnathan.taskly.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.codewithnathan.taskly.data.Task
import com.codewithnathan.taskly.data.TaskDao
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

@OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
class TaskListViewModel(taskDao: TaskDao) : ViewModel() {

    private val searchQuery = MutableStateFlow("")

    val taskListUiState: StateFlow<TaskListUiState> = searchQuery
        .debounce(300) // Debounce for smoother search input handling
        .flatMapLatest { query ->
            taskDao.getAllTasks()
                .map { tasks ->
                    if (query.isBlank()) {
                        TaskListUiState(tasks) // Return all tasks if query is empty
                    } else {
                        val filteredTasks = tasks.filter { it.matchesQuery(query) }
                        TaskListUiState(filteredTasks)
                    }
                }
        }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            TaskListUiState() // Initial empty state
        )

    fun searchTask(query: String) {
        searchQuery.value = query
    }
}

fun Task.matchesQuery(query: String): Boolean {
    return title.contains(query, ignoreCase = true) || notes.contains(query, ignoreCase = true)
}

data class TaskListUiState(val taskList: List<Task> = listOf())
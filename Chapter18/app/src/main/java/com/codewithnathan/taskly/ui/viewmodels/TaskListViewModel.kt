package com.codewithnathan.taskly.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.codewithnathan.taskly.data.Task
import com.codewithnathan.taskly.data.TaskDao
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
@OptIn(FlowPreview::class)
class TaskListViewModel @Inject constructor(
    private val taskDao: TaskDao
) : ViewModel() {

    private val searchQuery = MutableStateFlow("")
    val selectedFilter = MutableStateFlow(TaskFilter.ALL)
    private val selectedSort = MutableStateFlow(TaskSort.DESC)

    val taskListUiState: StateFlow<TaskListUiState> = combine(
        searchQuery.debounce(300), // Handle search query changes with debounce
        selectedFilter,            // Listen to filter changes
        selectedSort,
        taskDao.getAllTasks()
    ) { query, filter, sort, tasks ->
        // Filter tasks based on the selected filter and search query
        val filteredTasks = tasks
            .filter { task ->
                when (filter) {
                    TaskFilter.ALL -> true
                    TaskFilter.COMPLETED -> task.isCompleted
                    TaskFilter.INCOMPLETE -> !task.isCompleted
                }
            }
            .filter { task -> task.matchesQuery(query) }

        val sortedTasks = when (sort) {
            TaskSort.ASC -> filteredTasks.sortedBy { it.dueDate }
            TaskSort.DESC -> filteredTasks.sortedByDescending { it.dueDate }
        }

        TaskListUiState(sortedTasks)
    }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            TaskListUiState() // Initial empty state
        )

    fun searchTask(query: String) {
        searchQuery.value = query
    }

    fun setFilter(filter: TaskFilter) {
        selectedFilter.value = filter
    }

    fun setSort(sort: TaskSort) {
        selectedSort.value = sort
    }

    suspend fun updateTask(taskToUpdate: Task) {
        taskDao.update(taskToUpdate)
    }
}

enum class TaskSort {
    ASC,
    DESC
}

enum class TaskFilter {
    ALL,
    COMPLETED,
    INCOMPLETE
}

fun Task.matchesQuery(query: String): Boolean {
    return title.contains(query, ignoreCase = true) || notes.contains(query, ignoreCase = true)
}

data class TaskListUiState(val taskList: List<Task> = listOf())
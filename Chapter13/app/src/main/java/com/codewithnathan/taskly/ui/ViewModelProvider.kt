package com.codewithnathan.taskly.ui

import android.app.Application
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.codewithnathan.taskly.TasklyApplication
import com.codewithnathan.taskly.ui.viewmodels.TaskFormViewModel
import com.codewithnathan.taskly.ui.viewmodels.TaskListViewModel

object ViewModelProvider {
    val Factory = viewModelFactory {
        initializer {
            TaskListViewModel(tasklyApplication().container.taskDao)
        }
        initializer {
            TaskFormViewModel(tasklyApplication().container.taskDao)
        }
    }
}

/**
 * Extension function to queries for [Application] object and returns an instance of
 * [TasklyApplication].
 */
fun CreationExtras.tasklyApplication(): TasklyApplication =
    (this[AndroidViewModelFactory.APPLICATION_KEY] as TasklyApplication)

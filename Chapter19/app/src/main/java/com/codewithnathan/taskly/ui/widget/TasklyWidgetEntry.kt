package com.codewithnathan.taskly.ui.widget

import com.codewithnathan.taskly.data.TaskDao
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@EntryPoint
@InstallIn(SingletonComponent::class)
interface TasklyWidgetEntry {
    fun taskDao(): TaskDao
}
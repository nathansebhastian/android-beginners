package com.codewithnathan.taskly.data

import android.content.Context

class TasklyDataContainer(private val context: Context) {
    val taskDao: TaskDao by lazy {
        TasklyDatabase.getDatabase(context).taskDao()
    }
}
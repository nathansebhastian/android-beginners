package com.codewithnathan.taskly

import android.app.Application
import com.codewithnathan.taskly.data.TasklyDataContainer

class TasklyApplication: Application() {
    lateinit var container: TasklyDataContainer

    override fun onCreate() {
        super.onCreate()
        container = TasklyDataContainer(this)
    }
}
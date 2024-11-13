package com.codewithnathan.taskly.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Task::class], version = 3, exportSchema = false)
abstract class TasklyDatabase: RoomDatabase() {
    abstract fun taskDao(): TaskDao
}
package com.codewithnathan.taskly.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Task::class], version = 1, exportSchema = false)
abstract class TasklyDatabase: RoomDatabase() {
    abstract fun taskDao(): TaskDao
    companion object {
        @Volatile
        private var Instance: TasklyDatabase? = null
        fun getDatabase(context: Context): TasklyDatabase {
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(context, TasklyDatabase::class.java, "task_database")
                    .fallbackToDestructiveMigration()
                    .build()
                    .also { Instance = it }
            }
        }
    }
}
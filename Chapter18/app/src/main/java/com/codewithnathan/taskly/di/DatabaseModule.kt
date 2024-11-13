package com.codewithnathan.taskly.di

import android.content.Context
import androidx.room.Room
import com.codewithnathan.taskly.data.TaskDao
import com.codewithnathan.taskly.data.TasklyDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun providesAppDatabase(@ApplicationContext context: Context): TasklyDatabase {
        return Room.databaseBuilder(
            context,
            TasklyDatabase::class.java,
            "task_database"
        )
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    @Singleton
    fun provideTaskDao(appDatabase: TasklyDatabase): TaskDao {
        return appDatabase.taskDao()
    }
}
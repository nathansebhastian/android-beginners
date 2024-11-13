package com.codewithnathan.taskly.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Entity(tableName = "tasks")
data class Task(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val notes: String,
    val isCompleted: Boolean,
    val dueDate: Long,
    val imageUri: String? = null
)

fun getFormattedDueDate(millis: Long): String {
    val dateFormatter = SimpleDateFormat.getDateInstance(DateFormat.SHORT, Locale.getDefault())
    return dateFormatter.format(Date(millis))
}
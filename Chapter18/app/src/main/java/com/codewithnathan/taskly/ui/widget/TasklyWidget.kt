package com.codewithnathan.taskly.ui.widget

import android.content.Context
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.GlanceTheme
import androidx.glance.ImageProvider
import androidx.glance.action.ActionParameters
import androidx.glance.action.actionParametersOf
import androidx.glance.action.actionStartActivity
import androidx.glance.action.clickable
import androidx.glance.appwidget.CheckBox
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.action.ActionCallback
import androidx.glance.appwidget.action.actionRunCallback
import androidx.glance.appwidget.components.Scaffold
import androidx.glance.appwidget.components.TitleBar
import androidx.glance.appwidget.provideContent
import androidx.glance.appwidget.updateAll
import androidx.glance.layout.Alignment
import androidx.glance.layout.Column
import androidx.glance.layout.Row
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.fillMaxWidth
import androidx.glance.layout.padding
import androidx.glance.text.FontStyle
import androidx.glance.text.FontWeight
import androidx.glance.text.Text
import androidx.glance.text.TextAlign
import androidx.glance.text.TextStyle
import com.codewithnathan.taskly.MainActivity
import com.codewithnathan.taskly.R
import com.codewithnathan.taskly.data.Task
import com.codewithnathan.taskly.data.getFormattedDueDate
import dagger.hilt.android.EntryPointAccessors
import kotlinx.coroutines.flow.Flow

class TasklyWidget: GlanceAppWidget() {
    override suspend fun provideGlance(context: Context, id: GlanceId) {
        val taskFlow = fetchTasks(context)
        provideContent {
            val taskData by taskFlow.collectAsState(emptyList())
            GlanceTheme {
                Scaffold(
                    titleBar = {TitleBar(
                        startIcon = ImageProvider(R.drawable.baseline_android_24),
                        title = "Taskly"
                    )},
                    modifier = GlanceModifier.fillMaxSize()) {
                    Column {
                        if(taskData.isEmpty()) {
                            Text(
                                text = "No incomplete tasks. Hurray!\nTap here to create a new task",
                                style = TextStyle(
                                    fontWeight = FontWeight.Medium,
                                    fontSize = 16.sp,
                                    textAlign = TextAlign.Center
                                ),
                                modifier = GlanceModifier
                                    .fillMaxWidth()
                                    .padding(16.dp)
                                    .clickable(
                                        actionStartActivity<MainActivity>()
                                    )
                            )
                        } else {
                            taskData.forEach { task ->
                                Row (
                                    modifier = GlanceModifier
                                        .fillMaxWidth()
                                        .padding(bottom = 8.dp),
                                    horizontalAlignment = Alignment.Start
                                ) {
                                    Text(
                                        text = task.title,
                                        style = TextStyle(fontWeight = FontWeight.Bold),
                                        modifier = GlanceModifier.defaultWeight())
                                    Text(
                                        text = getFormattedDueDate(task.dueDate),
                                        style = TextStyle(fontStyle = FontStyle.Italic),
                                        modifier = GlanceModifier.defaultWeight()
                                    )
                                    CheckBox(
                                        checked = false,
                                        onCheckedChange = actionRunCallback<CompleteTaskAction>(
                                            actionParametersOf(ActionParameters.Key<Int>("taskId") to task.id)
                                        )
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private fun fetchTasks(context: Context): Flow<List<Task>> {
        val hiltEntryPoint = EntryPointAccessors.fromApplication(
            context,
            TasklyWidgetEntry::class.java
        )
        val taskDao = hiltEntryPoint.taskDao()

        return taskDao.getWidgetTasks()
    }
}

class CompleteTaskAction : ActionCallback {
    override suspend fun onAction(
        context: Context,
        glanceId: GlanceId,
        parameters: ActionParameters
    ) {
        val taskId = parameters[ActionParameters.Key("taskId")] as Int

        // Complete the task in the database
        completeTask(context, taskId)

        // Update the widget after completing the task
        TasklyWidget().updateAll(context)
    }
    /**
     * Mark the task as completed in the database.
     */
    private suspend fun completeTask(context: Context, taskId: Int) {
        val hiltEntryPoint = EntryPointAccessors.fromApplication(
            context,
            TasklyWidgetEntry::class.java
        )
        val taskDao = hiltEntryPoint.taskDao()

        taskDao.widgetCompleteTask(taskId)
    }
}
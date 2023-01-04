package com.rodrigo.madailytasks.core

import com.rodrigo.madailytasks.collections.TaskItem
import com.rodrigo.madailytasks.collections.TimeTask

interface TasksRepository {

    fun fetchTasks(): List<TaskItem>

    fun getTask(taskId: String): TaskItem

    fun toggleTaskIsRunning(id: String)

    fun addTask(
        task: String,
        subTask: String,
        tag: String,
        project: String,
        time: TimeTask
    )
}

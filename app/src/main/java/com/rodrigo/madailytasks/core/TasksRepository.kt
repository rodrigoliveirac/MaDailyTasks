package com.rodrigo.madailytasks.core

import com.rodrigo.madailytasks.collections.TaskItem
import kotlinx.coroutines.flow.Flow

interface TasksRepository {

    suspend fun fetchTasks(): Flow<List<TaskItem>>
    fun timerTest(id:String, position: Int)
    fun addTask(
        task: String,
        subTask: String,
        tag: String,
        project: String,
        time: Long
    )
}

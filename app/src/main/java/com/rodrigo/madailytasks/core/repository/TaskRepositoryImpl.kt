package com.rodrigo.madailytasks.core.repository

import com.rodrigo.madailytasks.collections.Tag
import com.rodrigo.madailytasks.core.model.TaskDomain
import java.util.*

object TaskRepositoryImpl : TaskRepository {

    private val taskListCache : MutableList<TaskDomain> = mutableListOf()

    override suspend fun fetchAll() = taskListCache.map { it.copy() }

    override suspend fun add(
        task: String,
        subTask: String,
        tag: String,
        project: String,
        time: Long
    ) {
        val taskItem = TaskDomain(
            id = UUID.randomUUID().toString(),
            task = task,
            subtask = subTask,
            tag = Tag.valueOf(tag),
            project = project,
            timeTask = time,
            isRunning = false,
            isDone = false,
        )

        taskListCache.add(taskItem)
    }
}
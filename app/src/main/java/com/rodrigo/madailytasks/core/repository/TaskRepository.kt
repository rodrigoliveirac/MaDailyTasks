package com.rodrigo.madailytasks.core.repository

import com.rodrigo.madailytasks.core.model.TaskDomain

interface TaskRepository {

    suspend fun fetchAll() : List<TaskDomain>

    suspend fun add(
        task: String,
        subTask: String,
        tag: String,
        project: String,
        time: Long
    )
}
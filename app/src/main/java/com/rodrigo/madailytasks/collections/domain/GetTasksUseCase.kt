package com.rodrigo.madailytasks.collections.domain

import com.rodrigo.madailytasks.collections.TaskItem

interface GetTasksUseCase {

    suspend operator fun invoke(): List<TaskItem>

}
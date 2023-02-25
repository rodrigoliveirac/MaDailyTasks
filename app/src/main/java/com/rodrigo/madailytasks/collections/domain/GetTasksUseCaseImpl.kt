package com.rodrigo.madailytasks.collections.domain

import com.rodrigo.madailytasks.collections.TaskItem
import com.rodrigo.madailytasks.core.repository.TaskRepository

class GetTasksUseCaseImpl(
    private val taskRepository: TaskRepository
) : GetTasksUseCase {

    override suspend fun invoke(): List<TaskItem> {
        return taskRepository.fetchAll().map { task ->

            TaskItem(
                id = task.id,
                task = task.task,
                subtask = task.subtask,
                project = task.project,
                tag = task.tag,
                timeTask = task.timeTask,
                isRunning = task.isRunning,
                isDone = task.isDone
            )
        }
    }
}
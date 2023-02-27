package com.rodrigo.madailytasks.collections.domain

import com.rodrigo.madailytasks.collections.TaskItem
import com.rodrigo.madailytasks.core.model.TaskDomain
import com.rodrigo.madailytasks.core.model.TimerDomain
import com.rodrigo.madailytasks.core.repository.TaskRepository
import com.rodrigo.madailytasks.core.repository.TimerRepository

class GetTasksUseCaseImpl(
    private val taskRepository: TaskRepository,
    private val timerRepository: TimerRepository
) : GetTasksUseCase {

    override suspend fun invoke(): List<TaskItem> {
        return taskRepository.fetchAll().map { task ->

            // get item by id
            val item = timerRepository.fetch(task.id)

            mapIsRunningValue(item, task)

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

    private fun mapIsRunningValue(
        item: List<TimerDomain>,
        task: TaskDomain
    ) {
        item.map {
            task.isRunning = it.isRunning
        }
    }
}
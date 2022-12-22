package com.rodrigo.madailytasks.dummy

import com.rodrigo.madailytasks.collections.Tag
import com.rodrigo.madailytasks.collections.TaskItem
import com.rodrigo.madailytasks.collections.TimeTask
import com.rodrigo.madailytasks.core.TasksRepository
import java.util.*

/**
 * Mock data with [TaskItem] for the collection.
 */
object MockTasks : TasksRepository {

    private val taskItemList: MutableList<TaskItem> = mutableListOf()

    override fun fetchTasks() = taskItemList.map { it.copy() }

    override fun addTask(
        task: String,
        subTask: String,
        tag: String,
        project: String,
        time: TimeTask
    ) {
        taskItemList.add(
            TaskItem(
                id = UUID.randomUUID().toString(),
                task = task,
                subtask = subTask,
                tag = Tag.valueOf(tag),
                project = project,
                time = time,
                isDone = false
            )
        )
    }

    override fun toggleTaskFinished(id: String) {
        val taskIndex = findTaskIndexById(id)
        val task = taskItemList[taskIndex]
        taskItemList[taskIndex] = task.copy(isDone = !task.isDone)
    }

    private fun findTaskIndexById(id: String) = taskItemList.indexOfFirst { taskItem ->
        taskItem.id == id
    }
}

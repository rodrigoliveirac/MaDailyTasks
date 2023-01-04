package com.rodrigo.madailytasks.dummy

import android.os.CountDownTimer
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

    override fun getTask(taskId: String): TaskItem {
        val taskIndex = findTaskIndexById(taskId)
        val task = taskItemList[taskIndex]
        return task.copy(task = task.task, subtask = task.subtask, project = task.project, tag = Tag.valueOf(task.tag.name), timeTask = task.timeTask)
    }

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
                timeTask = time,
                isDone = false,
            )
        )
    }

    override fun toggleTaskIsRunning(id: String) {
        val taskIndex = findTaskIndexById(id)
        val task = taskItemList[taskIndex]
        taskItemList[taskIndex] = task.copy(isRunning = !task.isRunning)
    }

    private fun findTaskIndexById(id: String) = taskItemList.indexOfFirst { taskItem ->
        taskItem.id == id
    }
}

package com.rodrigo.madailytasks.dummy

import android.os.CountDownTimer
import com.rodrigo.madailytasks.collections.Tag
import com.rodrigo.madailytasks.collections.TaskItem
import com.rodrigo.madailytasks.core.TasksRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import java.util.*

/**
 * Mock data with [TaskItem] for the collection.
 */
object MockTasks : TasksRepository {

    private var taskItemList: MutableList<TaskItem> = mutableListOf()

    private var taskItemListFlow = MutableStateFlow(getTasks())
    private fun getTasks() = taskItemList.map { it.copy() }


    private val runningTimers = HashMap<Int, CountDownTimer>()

    private var currentRunningTask = -1

    override suspend fun fetchTasks(): Flow<List<TaskItem>> =
        taskItemListFlow


    override fun addTask(
        task: String,
        subTask: String,
        tag: String,
        project: String,
        time: Long
    ) {
        taskItemList.add(
            TaskItem(
                id = UUID.randomUUID().toString(),
                task = task,
                subtask = subTask,
                tag = Tag.valueOf(tag),
                project = project,
                timeTask = time,
                isRunning = false,
                isDone = false,
            )
        )
        taskItemListFlow.value = taskItemList
    }

    override fun setupCurrentTimers(id: String, position: Int) {

        isRunningOrNot(id)

        val taskIndex = findHabitIndexById(id)
        val task = taskItemList[taskIndex]

        runningTimers.values.forEach { it.cancel() }

        val currentTimer = runningTimers[position]

        setupTimer(currentTimer, position, task)

    }

    private fun isRunningOrNot(id: String) {
        taskItemList = taskItemList.map {
            if (id == it.id) {
                it.copy(isRunning = !it.isRunning)
            } else {
                it.copy(isRunning = false)

            }
        } as MutableList<TaskItem>
        taskItemListFlow.value = taskItemList
    }

    private fun setupTimer(
        currentTimer: CountDownTimer?,
        position: Int,
        task: TaskItem,
    ) {

        if (currentTimer != null && position == currentRunningTask) {
            if (currentRunningTask != -1) {
                currentRunningTask = -1
                currentTimer.cancel()
            }
        } else {
            startTimer(task, position)
        }
    }

    private fun startTimer(task: TaskItem, position: Int) {

        val index = findHabitIndexById(task.id)
        val taskItem = taskItemList[index]

        val timer = object : CountDownTimer(taskItem.timeTask, 1000) {

            override fun onTick(millisUntilFinished: Long) {

                taskItemList = taskItemList.map {
                    if (task.id == it.id) {
                        it.copy(timeTask = millisUntilFinished)
                    } else {
                        it.copy()
                    }
                } as MutableList<TaskItem>

                taskItemListFlow.value = taskItemList
            }

            override fun onFinish() {
                taskItemList = taskItemList.map {
                    if (task.id == it.id) {
                        it.copy(isDone = true, isRunning = false)
                    } else {
                        it.copy()
                    }
                } as MutableList<TaskItem>
                taskItemListFlow.value = taskItemList
            }
        }
        runningTimers[position] = timer
        timer.start()
        currentRunningTask = position
    }

    private fun findHabitIndexById(id: String) = taskItemList.indexOfFirst { taskItem ->
        taskItem.id == id
    }
}

package com.rodrigo.madailytasks.collections.domain

import android.os.CountDownTimer
import com.rodrigo.madailytasks.core.repository.TaskRepository
import com.rodrigo.madailytasks.core.repository.TimerRepository
import java.util.HashMap

class ToggleTimerUseCaseImpl(
    private val taskList: TaskRepository,
    private val timerRepository: TimerRepository
) : ToggleTimerUseCase {

    private lateinit var timer: CountDownTimer

    private val runningTimers: HashMap<Int, CountDownTimer> = hashMapOf()

    private var currentRunningTask = -1

    override suspend fun invoke(taskId: String) {

        val taskIndex = findHabitIndexById(taskId)
        val task = taskList.fetchAll()[taskIndex]

        val timer = timerRepository.fetch(taskId)


        if (timer.isNotEmpty()) {
            timerRepository.delete(timer.first().id)
        } else {
            timerRepository.add(taskId = taskId)
        }

    }

    private suspend fun findHabitIndexById(id: String) =
        taskList.fetchAll().indexOfFirst { taskItem ->
            taskItem.id == id
        }
}
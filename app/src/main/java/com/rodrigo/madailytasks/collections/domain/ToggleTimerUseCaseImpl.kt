package com.rodrigo.madailytasks.collections.domain

import android.os.CountDownTimer
import com.rodrigo.madailytasks.core.repository.TaskRepository
import com.rodrigo.madailytasks.core.repository.TimerRepository
import java.util.HashMap

class ToggleTimerUseCaseImpl(
    private val taskList: TaskRepository,
    private val timerRepository: TimerRepository
) : ToggleTimerUseCase {

    override suspend fun invoke(taskId: String) {

        val timer = timerRepository.fetch(taskId)

        if (timer.isNotEmpty()) {
            timerRepository.setIfIsRunningOrNot(timer.first().id)
        } else {
            timerRepository.addAndStartRunning(taskId = taskId)
        }

    }
}
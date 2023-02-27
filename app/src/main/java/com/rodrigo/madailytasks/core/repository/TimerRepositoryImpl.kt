package com.rodrigo.madailytasks.core.repository

import android.os.CountDownTimer
import com.rodrigo.madailytasks.collections.TaskItem
import com.rodrigo.madailytasks.core.model.TimerDomain
import com.rodrigo.madailytasks.dummy.MockTasks
import kotlinx.coroutines.launch
import java.util.*

object TimerRepositoryImpl : TimerRepository {

    private var timerListCache: MutableList<TimerDomain> = mutableListOf()


    override suspend fun fetch(taskId: String): List<TimerDomain> {
        return timerListCache.filter {
            it.taskId == taskId
        }
    }

    override suspend fun setIfIsRunningOrNot(id: String) {
        timerListCache = timerListCache.map {
            if (it.id == id) {
                it.copy(isRunning = !it.isRunning)
            } else {
                it.copy(isRunning = false)
            }
        } as MutableList<TimerDomain>
    }

    override suspend fun addAndStartRunning(taskId: String) {

        timerListCache = timerListCache.map {
            it.copy(isRunning = false)
        } as MutableList<TimerDomain>

        val timer = TimerDomain(
            id = UUID.randomUUID().toString(),
            taskId = taskId,
            isRunning = true
        )

        timerListCache.add(timer)


    }


}
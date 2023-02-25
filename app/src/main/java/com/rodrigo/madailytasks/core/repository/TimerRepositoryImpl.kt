package com.rodrigo.madailytasks.core.repository

import com.rodrigo.madailytasks.core.model.TimerDomain
import java.util.*

object TimerRepositoryImpl : TimerRepository {

    private val timerListCache: MutableList<TimerDomain> = mutableListOf()

    override suspend fun fetch(taskId: String): List<TimerDomain> {
        return timerListCache.filter {
            it.taskId == taskId
        }
    }

    override suspend fun delete(id: String) {
        timerListCache.removeAll { it.id == id }
    }

    override suspend fun deleteOthers(id: String) {
        timerListCache.removeAll { it.id != id }
    }

    override suspend fun add(taskId: String) {

        val timer = TimerDomain(
            id = UUID.randomUUID().toString(),
            taskId = taskId,
            isRunning = true
        )
        timerListCache.add(timer).also {
            timerListCache.map {
                if (it.id != taskId) {
                    it.isRunning = false
                }
                TODO("RESOLVE THIS LOGIC: JUST ONE ITEM SHOULD RUNNING AT THE TIME")
            }
        }
    }

}
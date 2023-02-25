package com.rodrigo.madailytasks.core.repository

import com.rodrigo.madailytasks.core.model.TimerDomain

interface TimerRepository {

    /**
     * @param taskId ID of the specific Task
     */
    suspend fun fetch(taskId: String): List<TimerDomain>

    /**
     * @param id ID of the current timer
     */
    suspend fun delete(id: String)

    suspend fun deleteOthers(id: String)

    /**
     * @param taskId ID of the specific Task that current running
     */
    suspend fun add(taskId: String)

}
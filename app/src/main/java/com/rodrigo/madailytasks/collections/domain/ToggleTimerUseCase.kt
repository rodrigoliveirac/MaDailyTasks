package com.rodrigo.madailytasks.collections.domain

interface ToggleTimerUseCase {

    suspend operator fun invoke(taskId: String)
}
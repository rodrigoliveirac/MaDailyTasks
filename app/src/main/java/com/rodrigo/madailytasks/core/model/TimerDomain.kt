package com.rodrigo.madailytasks.core.model

data class TimerDomain(
    val id: String,
    val taskId: String,
    var isRunning: Boolean
//    val taskIndex: Int,
//    val currentTime: Long
)

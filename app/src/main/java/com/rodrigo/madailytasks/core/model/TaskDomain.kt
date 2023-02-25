package com.rodrigo.madailytasks.core.model

import com.rodrigo.madailytasks.collections.Tag

data class TaskDomain(
    var id: String,
    var task: String,
    val subtask: String,
    val project: String,
    val tag: Tag,
    var timeTask: Long,
    var isRunning: Boolean,
    var isDone: Boolean,
)
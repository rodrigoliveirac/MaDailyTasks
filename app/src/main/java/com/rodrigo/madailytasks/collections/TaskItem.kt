package com.rodrigo.madailytasks.collections

import com.rodrigo.madailytasks.R

/**
 * Task Model representing an Item in a ListView.
 *
 * @param task the name of the task
 * @param isDone whether the task is checked or not
 */
data class TaskItem(
    val id: String,
    val task: String,
    val subtask: String,
    val project: String,
    val tag: Tag,
    val time: TimeTask,
    val isDone: Boolean  = false //TODO("to do review of logic structure")
)

data class TimeTask(
    val hours: String,
    val minutes: String,
    val seconds: String
)

enum class Tag(val tag: Int) {

    WORKOUT(R.drawable.ic_icon_barbell_circle),
    STUDY(R.drawable.ic_icon_book_circle),
    WORK(R.drawable.ic_icon_monitor_circle),
    CODE(R.drawable.ic_icon_code_circle)


}

/*
    Hours =
    Seconds = Milliseconds / 1000
    Minutes = Seconds / 60
     or
     Minutes = (Milliseconds / 1000) / 60
 */

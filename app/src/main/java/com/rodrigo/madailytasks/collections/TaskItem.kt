package com.rodrigo.madailytasks.collections

import android.os.CountDownTimer
import com.rodrigo.madailytasks.R

/**
 * Task Model representing an Item in a ListView.
 *
 * @param task the name of the task
 * @param isDone whether the task is checked or not
 */
data class TaskItem(
    var id: String,
    var task: String,
    val subtask: String,
    val project: String,
    val tag: Tag,
    val timeTask: TimeTask,
    var countDownTimer: CountDownTimer? = null,
    var isRunning: Boolean = false,
    val isDone: Boolean  = false //TODO("to do review of logic structure")
)

data class CurrentTask(
    var taskId: TaskItem
)

data class TimeTask(
    val hours: String,
    val minutes: String,
    val seconds: String,
)

enum class RunningState() {
     PAUSED,
     STARTED
}

enum class Tag(val tag: Int) {

    WORKOUT(R.drawable.ic_icon_barbell_circle),
    STUDY(R.drawable.ic_icon_book_circle),
    WORK(R.drawable.ic_icon_monitor_circle),
    CODE(R.drawable.ic_icon_code_circle)


}

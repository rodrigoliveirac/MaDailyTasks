package com.rodrigo.madailytasks.collections

import android.os.CountDownTimer
import androidx.lifecycle.*
import com.rodrigo.madailytasks.R
import com.rodrigo.madailytasks.core.TasksRepository
import com.rodrigo.madailytasks.dummy.MockTasks
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*

/**
 * @see [https://developer.android.com/topic/libraries/architecture/viewmodel]
 */
class TaskListViewModel(private val repository: TasksRepository) : ViewModel() {

    /**
     * Mutable Live Data that initialize with the current list of saved Tasks.
     */
    private val uiState: MutableLiveData<UiState> by lazy {
        MutableLiveData<UiState>(UiState(taskItemList = repository.fetchTasks()))
    }

    private val uiStateTimer: MutableLiveData<UiStateTimer> by lazy {
        MutableLiveData<UiStateTimer>(
            UiStateTimer(
                currentTask = "",
                timeLeftFormatted = "00:00:00",
                timeInMs = 0L,
                isRunning = false,
            )
        )
    }

    private lateinit var countdownTimer: CountDownTimer

    fun stateOnceAndStreamTimer(): LiveData<UiStateTimer> {
        return uiStateTimer
    }

    /**
     * Expose the uiState as LiveData to UI.
     */
    fun stateOnceAndStream(): LiveData<UiState> {
        return uiState
    }

    /**
     * Add new Random Task.
     *
     * @param task: The name you wanna give to this Task
     * @param taskDaysSelected: Which days do you wanna practice the Task TODO
     */
    fun addTask(
        task: String,
        subTask: String,
        tag: String,
        project: String,
        time: TimeTask,
    ) {
        repository.addTask(task, subTask, tag, project, time)
        refreshTaskList()
    }

    private fun refreshTaskList() {
        uiState.value?.let { currentUiState ->
            uiState.value = currentUiState.copy(
                taskItemList = repository.fetchTasks()
            )
        }
    }

    fun clickStartCountTimer(timeTask: String) {

        viewModelScope.launch {
            uiStateTimer.value?.let { currentUiStateTimer ->
                uiStateTimer.value = currentUiStateTimer.copy(
                    timeLeftFormatted = timeTask
                )
            }
        }

    }

    fun isRunning(id:String) {
        repository.toggleTaskIsRunning(id)
    }

//    fun getCountTimer(task: TaskItem) {
//        if (task.isRunning) {
//            pauseTimer(task)
//        } else {
//            startTimer(task)
//        }
//    }
//
//    private fun pauseTimer(task: TaskItem) : Int {
//        CoroutineScope(Dispatchers.Default).launch {
//            task.countDownTimer?.cancel()
//            task.isRunning = false
//        }
//        return R.drawable.ic_play
//    }
//
//    private fun startTimer(task: TaskItem) : Int {
//
//        task.countDownTimer = object : CountDownTimer(timeInMs, 1000) {
//            override fun onFinish() {
//                task.isRunning = false
//            }
//
//            override fun onTick(p0: Long) {
//                task.isRunning = true
//                timeInMs = p0
//                updateCountDownText()
//            }
//        }.start()
//
//        task.isRunning = true
//
//        return R.drawable.ic_pause
//    }

    /**
     * UI State containing every data needed to show Tasks.
     */
    data class UiState(val taskItemList: List<TaskItem>)

    data class UiStateTimer(
        var currentTask: String,
        var timeLeftFormatted: String?,
        var timeInMs: Long,
        val isRunning: Boolean = false,
    )

    /**
     * ViewModel Factory needed to provide Repository injection to ViewModel.
     */
    @Suppress("UNCHECKED_CAST")
    class Factory(private val repository: TasksRepository) : ViewModelProvider.Factory {

        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return TaskListViewModel(repository) as T
        }
    }
}

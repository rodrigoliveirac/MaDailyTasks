package com.rodrigo.madailytasks.collections

import androidx.lifecycle.*
import com.rodrigo.madailytasks.core.TasksRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*

/**
 * @see [https://developer.android.com/topic/libraries/architecture/viewmodel]
 */
class TaskListViewModel(private val repository: TasksRepository) : ViewModel() {

    fun onResume() {
        viewModelScope.launch {
            refreshTaskList()
        }
    }

    /**
     * Mutable Live Data that initialize with the current list of saved Tasks.
     */
    private val uiState: MutableLiveData<UiState> by lazy {
        val list = listCollectByFlowFromRepository()
        MutableLiveData<UiState>(UiState(taskItemList = list))
    }

    private fun listCollectByFlowFromRepository(): List<TaskItem> {
        var list = emptyList<TaskItem>()

        viewModelScope.launch {
            repository.fetchTasks().collect {
                list = it
            }
        }
        return list
    }

    private val uiStateTime: MutableLiveData<UiStateTime> by lazy {

        MutableLiveData<UiStateTime>(UiStateTime(currentTaskRunning = "00:00:00"))
    }

    /**
     * Expose the uiState as LiveData to UI.
     */
    fun stateOnceAndStream(): LiveData<UiState> {
        return uiState
    }

    fun stateOnceAndStreamCurrentTime(): LiveData<UiStateTime> {
        return uiStateTime
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
        time: Long,
    ) {
        viewModelScope.launch {
            repository.addTask(task, subTask, tag, project, time)
        }
    }

    private fun refreshTaskList() {
        viewModelScope.launch {
            uiState.value?.let { currentUiState ->
                repository.fetchTasks().collect {
                    uiState.value = currentUiState.copy(
                        taskItemList = it,
                    )
                }
            }
        }
    }

    fun playOrPauseTimer(id: String) {

        viewModelScope.launch {
            repository.setupCurrentTimers(id)
        }

        updateCurrentTimeTaskValue()

    }

    private fun countDownText(ms: Long): String {

        val hour = (ms / 1000) / 3600
        val minute = (ms / 1000 / 60) % 60
        val seconds = (ms / 1000) % 60

        return java.lang.String.format(
            Locale.getDefault(),
            "%02d:%02d:%02d",
            hour,
            minute,
            seconds
        )
    }

    private fun updateCurrentTimeTaskValue() {

        viewModelScope.launch {

            uiStateTime.value?.let { currentUiState ->

                repository.fetchTasks().collect {

                    it.filter { item -> item.isRunning }.map { currentTaskRunningTime ->

                        uiStateTime.value = currentUiState.copy(
                            currentTaskRunning = countDownText(currentTaskRunningTime.timeTask),
                        )

                    }
                }
            }
        }
    }

    data class UiState(val taskItemList: List<TaskItem>)

    data class UiStateTime(val currentTaskRunning: String)

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

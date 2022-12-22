package com.rodrigo.madailytasks.collections

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.rodrigo.madailytasks.core.TasksRepository

/**
 * @see [https://developer.android.com/topic/libraries/architecture/viewmodel]
 */
class TaskListViewModel(private val repository: TasksRepository) : ViewModel() {

    /**
     * Mutable Live Data that initialize with the current list of saved Tasks.
     */
    private val uiState: MutableLiveData<UiState> by lazy {
        MutableLiveData<UiState>(UiState(taskItemList = repository.fetchTasks(), time = TimeTask("","",""), isRunning = false))
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

    /**
     * UI State containing every data needed to show Tasks.
     */
    data class UiState(val taskItemList: List<TaskItem>, val time: TimeTask, val isRunning: Boolean)

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

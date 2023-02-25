package com.rodrigo.madailytasks.form

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.rodrigo.madailytasks.core.repository.TaskRepository
import kotlinx.coroutines.launch

class TaskFormViewModel(
    private val taskRepository: TaskRepository
) : ViewModel() {

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
            taskRepository.add(task, subTask, tag, project, time)
        }
    }

    /**
     * ViewModel Factory needed to provide Repository injection to ViewModel.
     */
    @Suppress("UNCHECKED_CAST")
    class Factory(
        private val taskRepository: TaskRepository,
    ) : ViewModelProvider.Factory {

        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return TaskFormViewModel(taskRepository) as T
        }
    }
}

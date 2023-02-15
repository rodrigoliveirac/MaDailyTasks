package com.rodrigo.madailytasks.collections

import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.rodrigo.madailytasks.core.TasksRepository
import com.rodrigo.madailytasks.dummy.MockTasks

class TaskListLifecycleObserver(
    private val viewModel: TaskListViewModel,
) : DefaultLifecycleObserver {

    override fun onResume(owner: LifecycleOwner) {
        super.onResume(owner)
        viewModel.onResume()
    }

}
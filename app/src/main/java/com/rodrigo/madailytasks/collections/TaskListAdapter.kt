package com.rodrigo.madailytasks.collections

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.rodrigo.madailytasks.R
import com.rodrigo.madailytasks.databinding.TaskItemBinding
import java.util.*


/**
 * RecyclerView adapter for displaying a list of Tasks.
 *
 * The UI is based on the [TaskItemBinding].
 * We use the [TaskItem] as a model for the binding.
 */
class TaskListAdapter(
    private val viewModel: TaskListViewModel,
) : RecyclerView.Adapter<TaskListAdapter.ViewHolder>() {

    private val asyncListDiffer: AsyncListDiffer<TaskItem> = AsyncListDiffer(this, DiffCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = TaskItemBinding.inflate(layoutInflater, parent, false)

        return ViewHolder(viewModel = viewModel, binding = binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
//        viewModel.stateOnceAndStream().observe(holder.itemView.context as LifecycleOwner) {
//            val list = it.taskItemList
//            val task = asyncListDiffer.currentList[position]
//            holder.bind(list[position], position)
//        }
        val task = asyncListDiffer.currentList[position]
        holder.bind(task, position)
    }

    override fun getItemCount(): Int = asyncListDiffer.currentList.size

    fun updateTasks(tasks: List<TaskItem>) {
        asyncListDiffer.submitList(tasks)
    }


    inner class ViewHolder(
        private val binding: TaskItemBinding,
        private val viewModel: TaskListViewModel
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(task: TaskItem, position: Int) {

            if (task.isRunning) {
                binding.btnStart.setImageResource(R.drawable.ic_pause)
            } else {
                binding.btnStart.setImageResource(R.drawable.ic_play)
            }

            binding.timeTextView.text = countDownText(task.timeTask)
            binding.taskNameTextView.text = task.task
            binding.tagNameTextView.text = task.tag.name
            binding.imgTag.setImageResource(task.tag.tag)
            binding.projectNameTextView.text = task.project

            binding.btnStart.setOnClickListener {

                viewModel.timerTest(task.id, position)

            }
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
    }
}


object DiffCallback : DiffUtil.ItemCallback<TaskItem>() {

    override fun areItemsTheSame(oldItem: TaskItem, newItem: TaskItem): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: TaskItem, newItem: TaskItem): Boolean {
        return oldItem.isRunning == newItem.isRunning && oldItem.timeTask == newItem.timeTask
    }
}




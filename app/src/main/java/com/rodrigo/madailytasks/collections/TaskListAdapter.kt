package com.rodrigo.madailytasks.collections

import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.rodrigo.madailytasks.R
import com.rodrigo.madailytasks.databinding.TaskItemBinding
import java.util.*
import java.util.concurrent.TimeUnit


/**
 * RecyclerView adapter for displaying a list of Tasks.
 *
 * The UI is based on the [TaskItemBinding].
 * We use the [TaskItem] as a model for the binding.
 */
class TaskListAdapter(
    private val viewModel: TaskListViewModel
) : RecyclerView.Adapter<TaskListAdapter.ViewHolder>() {

    private val asyncListDiffer: AsyncListDiffer<TaskItem> = AsyncListDiffer(this, DiffCallback)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = TaskItemBinding.inflate(layoutInflater, parent, false)
        return ViewHolder(binding, viewModel)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(asyncListDiffer.currentList[position])
    }

    override fun getItemCount(): Int = asyncListDiffer.currentList.size

    fun updateTasks(tasks: List<TaskItem>) {
        asyncListDiffer.submitList(tasks)
    }

    class ViewHolder(
        private val binding: TaskItemBinding,
        private val viewModel: TaskListViewModel
    ) : RecyclerView.ViewHolder(binding.root) {

        private var isRunning: Boolean = false;
        var isDone: Boolean = false;
        private lateinit var countdownTimer: CountDownTimer

        var timeInMs = 0L


        fun bind(task: TaskItem) {

            setValueTimeInMsByTask(task)

            binding.taskNameTextView.text = task.task
            binding.tagNameTextView.text = task.tag.name
            binding.imgTag.setImageResource(task.tag.tag)
            binding.projectNameTextView.text = task.project

            updateCountDownText()

            binding.btnStart.setOnClickListener {
                //viewModel.clickStartCountTimer(task.id)
                if (isRunning) {
                    pauseTimer()
                } else {
                    startTimer()
                }
            }


        }

        private fun setValueTimeInMsByTask(task: TaskItem) {
            val hours = task.time.hours.toLong() * 3600000L
            val min = task.time.minutes.toLong() * 60000L
            val sec = task.time.seconds.toLong() * 1000L

            val total = hours + min + sec
            timeInMs = total
        }

        private fun startTimer() {

            countdownTimer = object : CountDownTimer(timeInMs, 1000) {
                override fun onFinish() {
                    isDone = true
                }

                override fun onTick(p0: Long) {
                    timeInMs = p0
                    updateCountDownText()
                }
            }.start()

            isRunning = true
            binding.btnStart.setImageResource(R.drawable.ic_pause)

        }

        private fun updateCountDownText() {
            val hour = (timeInMs / 1000) / 3600
            val minute = (timeInMs / 1000 / 60) % 60
            val seconds = (timeInMs / 1000) % 60

            val timeLeftFormatted: String =
                java.lang.String.format(
                    Locale.getDefault(),
                    "%02d:%02d:%02d",
                    hour,
                    minute,
                    seconds
                )

            binding.timeTextView.text = timeLeftFormatted
        }

        private fun pauseTimer() {
            binding.btnStart.setImageResource(R.drawable.ic_play)
            countdownTimer.cancel()
            isRunning = false
        }
    }


    object DiffCallback : DiffUtil.ItemCallback<TaskItem>() {

        override fun areItemsTheSame(oldItem: TaskItem, newItem: TaskItem): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: TaskItem, newItem: TaskItem): Boolean {
            return oldItem.isDone == newItem.isDone
        }
    }
}

object Utility {

    //time to countdown - 1hr - 60secs
    const val TIME_COUNTDOWN: Long = 60000L
    private const val TIME_FORMAT = "%02d:%02d:%02d"


    //convert time to milli seconds
    fun Long.formatTime(): String = String.format(
        TIME_FORMAT,
        TimeUnit.MILLISECONDS.toHours(this),
        TimeUnit.MILLISECONDS.toMinutes(this) % 3600,
        TimeUnit.MILLISECONDS.toSeconds(this) % 60
    )

}

package com.rodrigo.madailytasks.form

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.rodrigo.madailytasks.R
import com.rodrigo.madailytasks.collections.TaskListViewModel
import com.rodrigo.madailytasks.databinding.FragmentTaskFormBinding
import com.rodrigo.madailytasks.dummy.MockTasks

/**
 * A [Fragment] that displays a list of habits.
 */
class TaskFormFragment : Fragment() {

    private var _binding: FragmentTaskFormBinding? = null

    private val binding get() = _binding!!

    private val viewModel: TaskListViewModel by activityViewModels {
        TaskListViewModel.Factory(MockTasks)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTaskFormBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupDropdownMenuTags()

        // Save Habit and Navigate Up
        binding.saveButton.setOnClickListener { onSave() }
    }

    private fun setupDropdownMenuTags() {
        val priorities = requireActivity().resources.getStringArray(R.array.tags)
        val arrayAdapter = ArrayAdapter(requireActivity(), R.layout.dropdown_item, priorities)
        val autoCompleteTextView = binding.autoCompleteTextView
        autoCompleteTextView.setAdapter(arrayAdapter)
    }

    private fun onSave() {
        // Get value from the input to save
        val taskName = binding.taskNameTextInput.editText?.text.toString()

        // Get period selected: where 1 is Monday and 7 is Sunday.
        val subtaskName = binding.subtaskNameTextInput.editText?.text.toString()

        val tagName = binding.tagNameTextInput.editText?.text.toString()

        val projectName = binding.projectNameTextInput.editText?.text.toString()

        val hours = binding.hoursTextInput.editText?.text.toString().toLong() * 3600000L
        val minutes = binding.minutesTextInput.editText?.text.toString().toLong() * 60000L
        val seconds = binding.secondsTextInput.editText?.text.toString().toLong() * 1000L

        val timeTask = hours + minutes + seconds


        // Use ViewModel to add the new Habit
        viewModel.addTask(taskName, subtaskName, tagName, projectName,timeTask)

        // Navigate Up in the navigation three, meaning: goes back
        findNavController().navigateUp()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

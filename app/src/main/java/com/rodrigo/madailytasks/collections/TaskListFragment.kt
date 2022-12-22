package com.rodrigo.madailytasks.collections

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.divider.MaterialDividerItemDecoration
import com.rodrigo.madailytasks.R
import com.rodrigo.madailytasks.databinding.FragmentTaskListBinding
import com.rodrigo.madailytasks.dummy.MockTasks

/**
 * A [Fragment] that displays a list of tasks.
 */
class TaskListFragment : Fragment() {

  private var _binding: FragmentTaskListBinding? = null

  private val binding get() = _binding!!

  private lateinit var adapter: TaskListAdapter

  private val viewModel: TaskListViewModel by activityViewModels {
    TaskListViewModel.Factory(MockTasks)
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    adapter = TaskListAdapter(viewModel)
  }

  override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
  ): View {
    _binding = FragmentTaskListBinding.inflate(inflater, container, false)
    return binding.root
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    // Set the adapter
    binding.taskRecyclerView.layoutManager = LinearLayoutManager(requireContext())
    binding.taskRecyclerView.adapter = adapter

    // Adding decorations to our recycler view
    addingDividerDecoration()

    // Observer UI State for changes.
    viewModel.stateOnceAndStream().observe(viewLifecycleOwner) {
      bindUiState(it)
    }

    // Set Navigation Fab
    binding.fab.setOnClickListener {
      findNavController().navigate(R.id.action_taskList_to_taskForm)
    }
  }

  /**
   * Bind UI State to View.
   *
   * Update list of tasks according to updates.
   */
  private fun bindUiState(uiState: TaskListViewModel.UiState) {
    adapter.updateTasks(uiState.taskItemList)
  }

  private fun addingDividerDecoration() {
    // Adding Line between items with MaterialDividerItemDecoration
    val divider = MaterialDividerItemDecoration(requireContext(), LinearLayoutManager.VERTICAL)

    // Adding the line at the end of the list
    divider.isLastItemDecorated = true

    val resources = requireContext().resources

    // Adding start spacing
    divider.dividerInsetStart = resources.getDimensionPixelSize(R.dimen.horizontal_margin)

    // Defining size of the line
    divider.dividerThickness = resources.getDimensionPixelSize(R.dimen.divider_height)
    divider.dividerColor = ContextCompat.getColor(requireContext(), R.color.primary_200)

    binding.taskRecyclerView.addItemDecoration(divider)
  }

  private fun addingDividerSpace() {
    binding.taskRecyclerView.addItemDecoration(TaskListItemDecoration(requireContext()))
  }

  override fun onDestroyView() {
    super.onDestroyView()
    _binding = null
  }
}

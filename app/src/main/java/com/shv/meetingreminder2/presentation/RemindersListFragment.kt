package com.shv.meetingreminder2.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.shv.meetingreminder2.R
import com.shv.meetingreminder2.databinding.FragmentRemindersListBinding
import com.shv.meetingreminder2.presentation.adapters.reminders.RemindersAdapter
import com.shv.meetingreminder2.presentation.viewmodels.reminders.RemindersViewModel

class RemindersListFragment : Fragment() {

    private var _binding: FragmentRemindersListBinding? = null
    private val binding: FragmentRemindersListBinding
        get() = _binding ?: throw RuntimeException("FragmentRemindersListBinding is null")

    private val adapter by lazy {
        RemindersAdapter()
    }

    private val viewModel by lazy {
        ViewModelProvider(this)[RemindersViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRemindersListBinding.inflate(
            layoutInflater,
            container,
            false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        observeViewModel()
        setupSwipeToDelete()

        binding.buttonAddReminder.setOnClickListener {
            findNavController().navigate(R.id.action_remindersListFragment_to_addReminderFragment)
        }
    }

    private fun observeViewModel() {
        viewModel.reminders.observe(viewLifecycleOwner) {
            if (it.isEmpty())
                binding.llEmptyList.visibility = View.VISIBLE
            else binding.llEmptyList.visibility = View.GONE

            adapter.submitList(it)
        }
    }

    private fun setupRecyclerView() {
        binding.rvRemindersList.adapter = adapter
    }

    private fun setupSwipeToDelete() {
        val swipeToItemCallback = object : ItemTouchHelper.SimpleCallback(
            0,
            ItemTouchHelper.LEFT
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val reminder = adapter.currentList[position]

                when (direction) {
                    ItemTouchHelper.LEFT -> {
                        viewModel.deleteReminder(reminder.id)

                        Toast.makeText(
                            requireContext(),
                            "\"${reminder.title}\" has been deleted",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }
        val itemTouchHelper = ItemTouchHelper(swipeToItemCallback)
        itemTouchHelper.attachToRecyclerView(binding.rvRemindersList)
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
package com.shv.meetingreminder2.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
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


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
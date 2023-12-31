package com.shv.meetingreminder2.presentation

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.shv.meetingreminder2.MeetingReminderApplication
import com.shv.meetingreminder2.R
import com.shv.meetingreminder2.databinding.FragmentRemindersListBinding
import com.shv.meetingreminder2.domain.entity.Reminder
import com.shv.meetingreminder2.presentation.adapters.reminders.RemindersAdapter
import com.shv.meetingreminder2.presentation.br.AlarmReceiver
import com.shv.meetingreminder2.presentation.viewmodels.ViewModelFactory
import com.shv.meetingreminder2.presentation.viewmodels.reminders.RemindersListState
import com.shv.meetingreminder2.presentation.viewmodels.reminders.RemindersViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

class RemindersListFragment : Fragment() {

    private var _binding: FragmentRemindersListBinding? = null
    private val binding: FragmentRemindersListBinding
        get() = _binding ?: throw RuntimeException("FragmentRemindersListBinding is null")

    private val adapter by lazy {
        RemindersAdapter()
    }

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val component by lazy {
        (requireActivity().application as MeetingReminderApplication).component
    }

    private val viewModel by lazy {
        ViewModelProvider(this, viewModelFactory)[RemindersViewModel::class.java]
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
        component.inject(this)
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        observeViewModel()
        setupSwipeToDelete()

        binding.buttonAddReminder.setOnClickListener {
            findNavController().navigate(R.id.action_remindersListFragment_to_addReminderFragment)
        }
    }

    private fun observeViewModel() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.RESUMED) {
                viewModel.reminders.collect {
                    when (it) {
                        RemindersListState.EmptyList -> {
                            binding.llEmptyList.visibility = View.VISIBLE
                        }

                        is RemindersListState.RemindersList -> {
                            binding.llEmptyList.visibility = View.GONE
                            adapter.submitList(it.remindersList)
                        }
                    }
                }
            }
        }
    }

    private fun setupRecyclerView() {
        binding.rvRemindersList.adapter = adapter
    }

    private fun setupSwipeToDelete() {
        val swipeToItemCallback = object : ItemTouchHelper.SimpleCallback(
            0,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
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
                            String.format(
                                requireContext().getString(R.string.toast_reminder_deleted),
                                reminder.title
                            ),
                            Toast.LENGTH_SHORT
                        ).show()
                        cancelNotification(reminder.id)
                    }

                    ItemTouchHelper.RIGHT -> {
                        launchEditReminderFragment(reminder)
                    }
                }
            }
        }
        val itemTouchHelper = ItemTouchHelper(swipeToItemCallback)
        itemTouchHelper.attachToRecyclerView(binding.rvRemindersList)
    }

    private fun launchEditReminderFragment(reminder: Reminder) {
        val bundle = bundleOf(
            AddReminderFragment.EDIT_REMINDER to reminder
        )
        findNavController().navigate(
            R.id.action_remindersListFragment_to_addReminderFragment,
            bundle
        )
    }

    private fun cancelNotification(reminderId: Int) {
        val alarmManager = requireActivity().getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = AlarmReceiver.newIntent(requireContext())
        val pendingIntent = PendingIntent.getBroadcast(
            requireContext(),
            reminderId,
            intent,
            PendingIntent.FLAG_IMMUTABLE
        )
        alarmManager.cancel(pendingIntent)
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
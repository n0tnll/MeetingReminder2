package com.shv.meetingreminder2.presentation

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.format.DateFormat.is24HourFormat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.NotificationManagerCompat
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.DateValidatorPointForward
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import com.shv.meetingreminder2.MeetingReminderApplication
import com.shv.meetingreminder2.R
import com.shv.meetingreminder2.data.extensions.getFullName
import com.shv.meetingreminder2.data.extensions.toDateString
import com.shv.meetingreminder2.data.extensions.toTimeString
import com.shv.meetingreminder2.databinding.FragmentAddReminderBinding
import com.shv.meetingreminder2.domain.entity.Client
import com.shv.meetingreminder2.domain.entity.Reminder
import com.shv.meetingreminder2.presentation.br.AlarmReceiver
import com.shv.meetingreminder2.presentation.viewmodels.ViewModelFactory
import com.shv.meetingreminder2.presentation.viewmodels.add_reminder.AddReminderFormState
import com.shv.meetingreminder2.presentation.viewmodels.add_reminder.AddReminderViewModel
import java.util.Calendar
import javax.inject.Inject

class AddReminderFragment : Fragment() {

    private var _binding: FragmentAddReminderBinding? = null
    private val binding: FragmentAddReminderBinding
        get() = _binding ?: throw RuntimeException("FragmentAddReminderBinding is null")

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val component by lazy {
        (requireActivity().application as MeetingReminderApplication).component
    }

    private val viewModel by lazy {
        ViewModelProvider(this, viewModelFactory)[AddReminderViewModel::class.java]
    }

    private val calendar by lazy {
        Calendar.getInstance().apply {
            set(Calendar.SECOND, 0)
        }
    }

    private lateinit var reminderTitle: String
    private lateinit var client: Client
    private var editedReminderId: Int? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddReminderBinding.inflate(
            layoutInflater,
            container,
            false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        component.inject(this)
        super.onViewCreated(view, savedInstanceState)

        observeViewModel()
        setTextWatchers()
        setOnClickListeners()
        getReminderToEdit()

        binding.etDateMeeting.setText(calendar.timeInMillis.toDateString())

    }

    private fun getReminderToEdit() {
        arguments?.let {
            val reminder = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                it.getParcelable(EDIT_REMINDER, Reminder::class.java)
            } else {
                it.getParcelable<Reminder>(EDIT_REMINDER)
            }

            reminder?.let { reminderFromBundle ->
                calendar.timeInMillis = reminderFromBundle.dateTime
                editedReminderId = reminderFromBundle.id
                client = reminderFromBundle.client
                setEditingReminderToForm(reminderFromBundle)
            }
        }
    }

    private fun setEditingReminderToForm(reminder: Reminder) {
        with(binding) {
            with(reminder) {
                etTitle.setText(title)
                etClient.setText(clientName)
                etDateMeeting.setText(dateTime.toDateString())
                etTimeMeeting.setText(dateTime.toTimeString())
            }
        }
    }

    private fun observeChosenClient() {
        val currentBackStackEntry = findNavController().currentBackStackEntry
        val savedStateHandle = currentBackStackEntry?.savedStateHandle
        savedStateHandle?.getLiveData<Client>(RESULT_CLIENT)
            ?.observe(currentBackStackEntry, Observer {
                viewModel.setClient(it)
            })
    }

    private fun observeViewModel() {
        with(binding) {
            viewModel.state.observe(viewLifecycleOwner) {
                when (it) {
                    is AddReminderFormState -> {
                        tiTitle.error = it.titleError
                        tiClient.error = it.clientNameError
                        tiTimeMeeting.error = it.timeError
                        buttonSave.isEnabled = it.isFormValid
                        if (it.isFinished) closeAddReminderFragment()
                    }
                }
            }
        }
        viewModel.client.observe(viewLifecycleOwner) {
            it?.let {
                val clientStr = String.format(
                    getString(R.string.client_field_template),
                    it.getFullName(),
                    it.email
                )
                binding.etClient.setText(clientStr)
                client = it
            }
        }
    }

    private fun setOnClickListeners() {
        observeChosenClient()
        with(binding) {
            etClient.setOnClickListener {
                findNavController().navigate(
                    AddReminderFragmentDirections.actionAddReminderFragmentToClientsListFragment()
                )
            }

            etDateMeeting.setOnClickListener {
                showDateDialogPicker()
            }

            etTimeMeeting.setOnClickListener {
                showTimeDialogPicker()
            }

            buttonSave.setOnClickListener {
                saveReminder()
            }
        }
    }

    private fun saveReminder() {
        val isTimeKnown = !binding.etTimeMeeting.text.isNullOrBlank()
        val reminder = if (editedReminderId == null) {
            Reminder(
                title = reminderTitle,
                clientName = client.getFullName(),
                dateTime = calendar.timeInMillis,
                isTimeKnown = isTimeKnown,
                client = client
            )
        } else {
            Reminder(
                id = editedReminderId ?: throw RuntimeException("Client is null"),
                title = reminderTitle,
                clientName = client.getFullName(),
                dateTime = calendar.timeInMillis,
                isTimeKnown = isTimeKnown,
                client = client
            )
        }
        if (editedReminderId != null) cancelNotification(reminder.id)
        viewModel.addReminder(reminder)
        setReminderAlarm(reminder)
    }

    private fun setTextWatchers() {
        with(binding) {
            etTitle.doOnTextChanged { text, _, _, _ ->
                viewModel.onEvent(AddReminderFormEvent.TitleChanged(text.toString()))
                reminderTitle = text.toString()
            }
            etClient.doOnTextChanged { text, _, _, _ ->
                viewModel.onEvent(AddReminderFormEvent.ClientChanged(text.toString()))
            }
            etDateMeeting.doOnTextChanged { _, _, _, _ ->
                viewModel.onEvent(AddReminderFormEvent.DateChanged(calendar))
                viewModel.onEvent(AddReminderFormEvent.TimeChanged(null))
            }
            etTimeMeeting.doOnTextChanged { text, _, _, _ ->
                if (!text.isNullOrBlank())
                    viewModel.onEvent(AddReminderFormEvent.TimeChanged(calendar))
                else {
                    viewModel.onEvent(AddReminderFormEvent.TimeChanged(null))
                }
            }
        }
    }

    private fun showDateDialogPicker() {
        val constraints = CalendarConstraints.Builder()
            .setValidator(DateValidatorPointForward.now())
            .build()

        val picker = MaterialDatePicker.Builder.datePicker()
            .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
            .setCalendarConstraints(constraints)
            .setTitleText("Select meeting date")
            .build()

        picker.addOnPositiveButtonClickListener {
            calendar.timeInMillis = it
            binding.etDateMeeting.setText(calendar.timeInMillis.toDateString())
        }
        picker.addOnCancelListener {
            binding.etDateMeeting.setText(EMPTY_FIELD)
        }

        picker.show(parentFragmentManager, DATE_PICKER_TAG)
    }

    private fun showTimeDialogPicker() {
        val isSystem24Hour = is24HourFormat(requireContext())
        val clockFormat = if (isSystem24Hour) TimeFormat.CLOCK_24H else TimeFormat.CLOCK_12H

        val picker = MaterialTimePicker.Builder()
            .setTimeFormat(clockFormat)
            .setTitleText("Select meeting time")
            .setHour(DEFAULT_HOUR)
            .setMinute(DEFAULT_MINUTES)
            .build()

        picker.addOnPositiveButtonClickListener {
            calendar.apply {
                set(Calendar.HOUR_OF_DAY, picker.hour)
                set(Calendar.MINUTE, picker.minute)
                set(Calendar.MILLISECOND, 0)
            }
            binding.etTimeMeeting.setText(calendar.timeInMillis.toTimeString())
        }

        picker.addOnCancelListener {
            binding.etTimeMeeting.setText(EMPTY_FIELD)
        }

        picker.show(parentFragmentManager, TIME_PICKER_TAG)
    }

    private fun closeAddReminderFragment() {
        findNavController().navigateUp()
    }

    private fun cancelNotification(id: Int) {
        NotificationManagerCompat.from(requireContext()).cancel(null, id)
    }

    private fun setReminderAlarm(reminder: Reminder) {
        val alarmManager = requireActivity().getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val meetingTime = Calendar.getInstance().apply {
            timeInMillis = reminder.dateTime
            if (reminder.isTimeKnown) {
                add(Calendar.HOUR_OF_DAY, -1)
                set(Calendar.SECOND, 0)
            } else {
                add(Calendar.HOUR_OF_DAY, 1)
                set(Calendar.SECOND, 0)
            }
        }
        val intent = AlarmReceiver.newIntent(requireContext()).apply {
            putExtra(ALARM_RECEIVER_EXTRA, reminder)
            flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
        }

        val pendingIntent =
            PendingIntent.getBroadcast(
                requireContext(),
                reminder.id,
                intent,
                PendingIntent.FLAG_IMMUTABLE
            )

        alarmManager.setExact(AlarmManager.RTC_WAKEUP, meetingTime.timeInMillis, pendingIntent)
        Toast.makeText(context, "Notification was added!", Toast.LENGTH_SHORT).show()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object {
        const val RESULT_CLIENT = "chosen_client"
        const val EDIT_REMINDER = "edit_reminder"
        const val ALARM_RECEIVER_EXTRA = "reminder_alarm"

        private const val DATE_PICKER_TAG = "date_picker"
        private const val TIME_PICKER_TAG = "time_picker"
        private const val EMPTY_FIELD = ""
        private const val DEFAULT_HOUR = 12
        private const val DEFAULT_MINUTES = 30
    }
}
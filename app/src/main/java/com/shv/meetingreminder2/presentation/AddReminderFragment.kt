package com.shv.meetingreminder2.presentation

import android.os.Bundle
import android.text.format.DateFormat.is24HourFormat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
import com.shv.meetingreminder2.R
import com.shv.meetingreminder2.data.extensions.getFullName
import com.shv.meetingreminder2.data.extensions.toDateString
import com.shv.meetingreminder2.data.extensions.toTimeString
import com.shv.meetingreminder2.databinding.FragmentAddReminderBinding
import com.shv.meetingreminder2.domain.entity.Client
import com.shv.meetingreminder2.domain.entity.Reminder
import com.shv.meetingreminder2.presentation.viewmodels.add_reminder.AddReminderFormState
import com.shv.meetingreminder2.presentation.viewmodels.add_reminder.AddReminderViewModel
import java.util.Calendar

class AddReminderFragment : Fragment() {

    private var _binding: FragmentAddReminderBinding? = null
    private val binding: FragmentAddReminderBinding
        get() = _binding ?: throw RuntimeException("FragmentAddReminderBinding is null")

    private val viewModel by lazy {
        ViewModelProvider(this)[AddReminderViewModel::class.java]
    }

    private val calendar by lazy {
        Calendar.getInstance().apply {
            set(Calendar.SECOND, 0)
        }
    }

    private lateinit var reminderTitle: String
    private lateinit var client: Client

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
        super.onViewCreated(view, savedInstanceState)

        observeViewModel()
        setTextWatchers()
        setOnClickListeners()

        binding.etDateMeeting.setText(calendar.timeInMillis.toDateString())


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
        val reminder = Reminder(
            title = reminderTitle,
            clientName = client.getFullName(),
            dateTime = calendar.timeInMillis,
            isTimeKnown = isTimeKnown,
            client = client
        )
        viewModel.addReminder(reminder)
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

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object {
        const val RESULT_CLIENT = "chosen_client"
        private const val DATE_PICKER_TAG = "date_picker"
        private const val TIME_PICKER_TAG = "time_picker"
        private const val EMPTY_FIELD = ""
        private const val DEFAULT_HOUR = 12
        private const val DEFAULT_MINUTES = 30
    }
}
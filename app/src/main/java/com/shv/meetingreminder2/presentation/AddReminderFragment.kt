package com.shv.meetingreminder2.presentation

import android.os.Bundle
import android.text.format.DateFormat.is24HourFormat
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.DateValidatorPointForward
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import com.shv.meetingreminder2.data.extensions.toDateString
import com.shv.meetingreminder2.data.extensions.toTimeString
import com.shv.meetingreminder2.databinding.FragmentAddReminderBinding
import java.util.Calendar

class AddReminderFragment : Fragment() {

    private var _binding: FragmentAddReminderBinding? = null
    private val binding: FragmentAddReminderBinding
        get() = _binding ?: throw RuntimeException("FragmentAddReminderBinding is null")

    private val calendar by lazy {
        Calendar.getInstance().apply {
            set(Calendar.SECOND, 0)
        }
    }

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

        binding.etClient.setOnClickListener {
            observeChosenClient()
            findNavController().navigate(
                AddReminderFragmentDirections.actionAddReminderFragmentToClientsListFragment()
            )
        }

        binding.etDateMeeting.setOnClickListener {
            showDateDialogPicker()
        }

        binding.etTimeMeeting.setOnClickListener {
            showTimeDialogPicker()
        }
    }

    private fun observeChosenClient() {
        val currentBackStackEntry = findNavController().currentBackStackEntry
        val savedStateHandle = currentBackStackEntry?.savedStateHandle
        savedStateHandle?.getLiveData<String>(RESULT_CLIENT)
            ?.observe(currentBackStackEntry, Observer {
                Log.d("CHOSEN_CLIENT", it)
            })
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
            .setHour(12)
            .setMinute(0)
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

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object {
        const val RESULT_CLIENT = "chosen_client"
        private const val DATE_PICKER_TAG = "date_picker"
        private const val TIME_PICKER_TAG = "time_picker"
        private const val EMPTY_FIELD = ""
    }
}
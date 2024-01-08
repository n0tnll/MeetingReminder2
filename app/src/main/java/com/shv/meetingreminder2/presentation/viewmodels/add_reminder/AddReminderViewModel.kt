package com.shv.meetingreminder2.presentation.viewmodels.add_reminder

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shv.meetingreminder2.data.extensions.toDateString
import com.shv.meetingreminder2.data.extensions.toTimeString
import com.shv.meetingreminder2.domain.entity.Reminder
import com.shv.meetingreminder2.domain.usecases.AddReminderUseCase
import com.shv.meetingreminder2.domain.usecases.validation.ValidateClientUseCase
import com.shv.meetingreminder2.domain.usecases.validation.ValidateDateTimeUseCase
import com.shv.meetingreminder2.domain.usecases.validation.ValidateTitleUseCase
import com.shv.meetingreminder2.presentation.AddReminderFormEvent
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class AddReminderViewModel @Inject constructor(
    private val validateTitleUseCase: ValidateTitleUseCase,
    private val validateClientUseCase: ValidateClientUseCase,
    private val validateDateTimeUseCase: ValidateDateTimeUseCase,
    private val addReminderUseCase: AddReminderUseCase,
) : ViewModel() {

    private val _state = MutableStateFlow(AddReminderFormState())
    val state = _state.asStateFlow()

    fun addReminder(reminder: Reminder) {
        viewModelScope.launch {
            addReminderUseCase(reminder)
            addedSuccess()
        }
    }

    fun onEvent(event: AddReminderFormEvent) {
        when (event) {
            is AddReminderFormEvent.TitleChanged -> {
                _state.value = _state.value.copy(title = event.title)
            }

            is AddReminderFormEvent.ClientChanged -> {
                _state.value = _state.value.copy(clientName = event.clientName)
            }

            is AddReminderFormEvent.DateChanged -> {
                _state.value = _state.value.copy(
                    date = event.date.timeInMillis.toDateString(),
                    dateTime = event.date
                )
            }

            is AddReminderFormEvent.TimeChanged -> {
                _state.value = _state.value.copy(
                    time = event.time?.timeInMillis?.toTimeString(),
                    dateTime = event.time
                )
            }
        }
        submitData()
    }

    private fun submitData() {
        val titleResult = validateTitleUseCase(_state.value.title)
        val clientResult = validateClientUseCase(_state.value.clientName)
        val timeResult = validateDateTimeUseCase(_state.value.dateTime)

        val hasError = listOf(
            titleResult,
            clientResult,
            timeResult
        ).any {
            !it.successful
        }

        _state.value = _state.value.copy(
            titleError = titleResult.errorMessage,
            clientNameError = clientResult.errorMessage,
            timeError = timeResult.errorMessage
        )

        if (hasError) {
            _state.value = _state.value.copy(isFormValid = false)
            return
        }

        _state.value = _state.value.copy(isFormValid = true)
    }

    private fun addedSuccess() {
        _state.value = _state.value.copy(isFinished = true)
    }
}
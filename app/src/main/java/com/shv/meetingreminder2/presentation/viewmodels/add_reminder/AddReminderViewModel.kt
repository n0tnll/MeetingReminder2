package com.shv.meetingreminder2.presentation.viewmodels.add_reminder

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.shv.meetingreminder2.data.extensions.toDateString
import com.shv.meetingreminder2.data.extensions.toTimeString
import com.shv.meetingreminder2.data.repositories.MeetingReminderRepositoryImpl
import com.shv.meetingreminder2.data.repositories.ReminderValidationRepositoryImpl
import com.shv.meetingreminder2.domain.entity.Client
import com.shv.meetingreminder2.domain.entity.Reminder
import com.shv.meetingreminder2.domain.usecases.AddReminderUseCase
import com.shv.meetingreminder2.domain.usecases.validation.ValidateClientUseCase
import com.shv.meetingreminder2.domain.usecases.validation.ValidateDateTimeUseCase
import com.shv.meetingreminder2.domain.usecases.validation.ValidateTitleUseCase
import com.shv.meetingreminder2.presentation.AddReminderFormEvent
import kotlinx.coroutines.launch

class AddReminderViewModel(
    application: Application
) : AndroidViewModel(application) {

    private val repository = ReminderValidationRepositoryImpl()
    private val validateTitleUseCase = ValidateTitleUseCase(repository)
    private val validateClientUseCase = ValidateClientUseCase(repository)
    private val validateDateTimeUseCase = ValidateDateTimeUseCase(repository)
    private val repositoryReminder = MeetingReminderRepositoryImpl(application)
    private val addReminderUseCase = AddReminderUseCase(repositoryReminder)

    private val _state = MutableLiveData<AddReminderFormState>()
    val state: LiveData<AddReminderFormState>
        get() = _state

    private val _client = MutableLiveData<Client?>()
    val client: LiveData<Client?>
        get() = _client

    init {
        _state.value = AddReminderFormState()
    }

    fun addReminder(reminder: Reminder) {
        viewModelScope.launch {
            addReminderUseCase(reminder)
            addedSuccess()
        }
    }

    fun setClient(client: Client?) {
        _client.value = client
    }

    fun onEvent(event: AddReminderFormEvent) {
        when (event) {
            is AddReminderFormEvent.TitleChanged -> {
                _state.value = _state.value?.copy(
                    title = event.title
                )
            }

            is AddReminderFormEvent.ClientChanged -> {
                _state.value = _state.value?.copy(
                    clientName = event.clientName
                )
            }

            is AddReminderFormEvent.DateChanged -> {
                _state.value = _state.value?.copy(
                    date = event.date.timeInMillis.toDateString(),
                    dateTime = event.date
                )
            }

            is AddReminderFormEvent.TimeChanged -> {
                _state.value = _state.value?.copy(
                    time = event.time?.timeInMillis?.toTimeString(),
                    dateTime = event.time
                )
            }
        }
        submitData()
    }

    private fun submitData() {
        val titleResult = validateTitleUseCase(_state.value?.title ?: "")
        val clientResult = validateClientUseCase(_state.value?.clientName ?: "")
        val timeResult = validateDateTimeUseCase(_state.value?.dateTime)

        val hasError = listOf(
            titleResult,
            clientResult,
            timeResult
        ).any {
            !it.successful
        }

        _state.value = _state.value?.copy(
            titleError = titleResult.errorMessage,
            clientNameError = clientResult.errorMessage,
            timeError = timeResult.errorMessage
        )

        if (hasError) {
            _state.value = _state.value?.copy(isFormValid = false)
            return
        }

        _state.value = _state.value?.copy(isFormValid = true)
    }

    private fun addedSuccess() {
        _state.value = _state.value?.copy(isFinished = true)
    }
}
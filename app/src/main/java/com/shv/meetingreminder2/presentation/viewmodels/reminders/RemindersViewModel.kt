package com.shv.meetingreminder2.presentation.viewmodels.reminders

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shv.meetingreminder2.domain.usecases.DeleteReminderUseCase
import com.shv.meetingreminder2.domain.usecases.GetRemindersListUseCase
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

class RemindersViewModel @Inject constructor(
    private val getRemindersListUseCase: GetRemindersListUseCase,
    private val deleteReminderUseCase: DeleteReminderUseCase
) : ViewModel() {
    val reminders = getRemindersListUseCase()
        .map {
            if (it.isEmpty()) {
                RemindersListState.EmptyList
            } else {
                RemindersListState.RemindersList(remindersList = it) as RemindersListState
            }
        }

    fun deleteReminder(id: Int) {
        viewModelScope.launch {
            deleteReminderUseCase(id)
        }
    }
}
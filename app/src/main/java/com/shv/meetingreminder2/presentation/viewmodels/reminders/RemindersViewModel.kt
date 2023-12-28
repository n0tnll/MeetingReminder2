package com.shv.meetingreminder2.presentation.viewmodels.reminders

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.shv.meetingreminder2.data.repositories.MeetingReminderRepositoryImpl
import com.shv.meetingreminder2.domain.usecases.DeleteReminderUseCase
import com.shv.meetingreminder2.domain.usecases.GetRemindersListUseCase
import kotlinx.coroutines.launch

class RemindersViewModel(
    application: Application
) : AndroidViewModel(application) {

    private val repository = MeetingReminderRepositoryImpl(application)
    private val getRemindersListUseCase = GetRemindersListUseCase(repository)
    private val deleteReminderUseCase = DeleteReminderUseCase(repository)

    val reminders = getRemindersListUseCase()

    fun deleteReminder(id: Int) {
        viewModelScope.launch {
            deleteReminderUseCase(id)
        }
    }
}
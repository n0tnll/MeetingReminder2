package com.shv.meetingreminder2.presentation.viewmodels.reminders

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.shv.meetingreminder2.data.MeetingReminderRepositoryImpl
import com.shv.meetingreminder2.domain.usecases.GetRemindersListUseCase

class RemindersViewModel(
    application: Application
) : AndroidViewModel(application) {

    private val repository = MeetingReminderRepositoryImpl(application)
    private val getRemindersListUseCase = GetRemindersListUseCase(repository)

    val reminders = getRemindersListUseCase()
}
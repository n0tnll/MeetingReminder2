package com.shv.meetingreminder2.presentation.viewmodels.reminders

import com.shv.meetingreminder2.domain.entity.Reminder

sealed class RemindersListState

data object EmptyList : RemindersListState()

data class RemindersList(
    val remindersList: List<Reminder>
) : RemindersListState()
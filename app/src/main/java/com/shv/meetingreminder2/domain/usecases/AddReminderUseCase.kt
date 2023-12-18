package com.shv.meetingreminder2.domain.usecases

import com.shv.meetingreminder2.domain.entity.Reminder
import com.shv.meetingreminder2.domain.repositories.MeetingReminderRepository

class AddReminderUseCase(private val repository: MeetingReminderRepository) {

    suspend operator fun invoke(reminder: Reminder) {
        repository.addReminder(reminder)
    }
}
package com.shv.meetingreminder2.domain.usecases

import com.shv.meetingreminder2.domain.entity.Reminder
import com.shv.meetingreminder2.domain.repositories.MeetingReminderRepository
import javax.inject.Inject

class AddReminderUseCase @Inject constructor(private val repository: MeetingReminderRepository) {

    suspend operator fun invoke(reminder: Reminder) {
        repository.addReminder(reminder)
    }
}
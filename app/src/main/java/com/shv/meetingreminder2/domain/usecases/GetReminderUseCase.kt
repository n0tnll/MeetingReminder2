package com.shv.meetingreminder2.domain.usecases

import com.shv.meetingreminder2.domain.entity.Reminder
import com.shv.meetingreminder2.domain.repositories.MeetingReminderRepository

class GetReminderUseCase(private val repository: MeetingReminderRepository) {

    suspend operator fun invoke(id: Int): Reminder {
        return repository.getReminder(id)
    }
}
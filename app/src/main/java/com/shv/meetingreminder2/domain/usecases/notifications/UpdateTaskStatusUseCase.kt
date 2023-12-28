package com.shv.meetingreminder2.domain.usecases.notifications

import com.shv.meetingreminder2.domain.entity.Reminder
import com.shv.meetingreminder2.domain.repositories.MeetingReminderRepository

class UpdateTaskStatusUseCase(private val repository: MeetingReminderRepository) {

    suspend operator fun invoke(reminder: Reminder) {
        repository.updateAlarmStatus(reminder)
    }
}
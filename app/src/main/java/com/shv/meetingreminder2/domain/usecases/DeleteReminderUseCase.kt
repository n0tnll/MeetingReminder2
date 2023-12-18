package com.shv.meetingreminder2.domain.usecases

import com.shv.meetingreminder2.domain.repositories.MeetingReminderRepository

class DeleteReminderUseCase(private val repository: MeetingReminderRepository) {

    suspend operator fun invoke(id: Int) {
        repository.deleteReminder(id)
    }
}
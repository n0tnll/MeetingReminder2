package com.shv.meetingreminder2.domain.usecases

import com.shv.meetingreminder2.domain.repositories.MeetingReminderRepository
import javax.inject.Inject

class DeleteReminderUseCase @Inject constructor(private val repository: MeetingReminderRepository) {

    suspend operator fun invoke(id: Int) {
        repository.deleteReminder(id)
    }
}
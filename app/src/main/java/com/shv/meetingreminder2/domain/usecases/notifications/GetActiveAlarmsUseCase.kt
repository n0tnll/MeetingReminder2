package com.shv.meetingreminder2.domain.usecases.notifications

import com.shv.meetingreminder2.domain.entity.Reminder
import com.shv.meetingreminder2.domain.repositories.MeetingReminderRepository
import javax.inject.Inject

class GetActiveAlarmsUseCase @Inject constructor(private val repository: MeetingReminderRepository) {

    suspend operator fun invoke(currentTime: Long): List<Reminder> {
        return repository.getActiveAlarms(currentTime)
    }
}
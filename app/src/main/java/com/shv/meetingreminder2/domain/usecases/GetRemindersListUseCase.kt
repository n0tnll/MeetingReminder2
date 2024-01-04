package com.shv.meetingreminder2.domain.usecases

import com.shv.meetingreminder2.domain.entity.Reminder
import com.shv.meetingreminder2.domain.repositories.MeetingReminderRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetRemindersListUseCase @Inject constructor(private val repository: MeetingReminderRepository) {

    operator fun invoke(): Flow<List<Reminder>> {
        return repository.getRemindersList()
    }
}
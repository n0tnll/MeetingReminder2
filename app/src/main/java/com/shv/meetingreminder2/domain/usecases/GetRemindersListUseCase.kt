package com.shv.meetingreminder2.domain.usecases

import androidx.lifecycle.LiveData
import com.shv.meetingreminder2.domain.entity.Reminder
import com.shv.meetingreminder2.domain.repositories.MeetingReminderRepository

class GetRemindersListUseCase(private val repository: MeetingReminderRepository) {

    operator fun invoke(): LiveData<List<Reminder>> {
        return repository.getRemindersList()
    }
}
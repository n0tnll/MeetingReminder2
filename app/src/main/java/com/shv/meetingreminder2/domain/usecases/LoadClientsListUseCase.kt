package com.shv.meetingreminder2.domain.usecases

import com.shv.meetingreminder2.domain.entity.Client
import com.shv.meetingreminder2.domain.repositories.MeetingReminderRepository

class LoadClientsListUseCase(private val repository: MeetingReminderRepository) {

    suspend operator fun invoke(): List<Client> {
        return repository.loadClientsList()
    }
}
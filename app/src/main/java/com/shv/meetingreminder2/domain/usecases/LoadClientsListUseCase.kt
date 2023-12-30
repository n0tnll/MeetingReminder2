package com.shv.meetingreminder2.domain.usecases

import com.shv.meetingreminder2.domain.entity.Client
import com.shv.meetingreminder2.domain.repositories.MeetingReminderRepository
import javax.inject.Inject

class LoadClientsListUseCase @Inject constructor(private val repository: MeetingReminderRepository) {

    suspend operator fun invoke(): List<Client> {
        return repository.loadClientsList()
    }
}
package com.shv.meetingreminder2.domain.usecases

import com.shv.meetingreminder2.domain.repositories.MeetingReminderRepository
import javax.inject.Inject

class RetryLoadClientsUseCase @Inject constructor(private val repository: MeetingReminderRepository) {

    suspend operator fun invoke() {
        repository.retryLoadClients()
    }
}
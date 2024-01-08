package com.shv.meetingreminder2.domain.usecases

import com.shv.meetingreminder2.domain.repositories.MeetingReminderRepository
import com.shv.meetingreminder2.presentation.viewmodels.clients.ClientsState
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LoadClientsListUseCase @Inject constructor(private val repository: MeetingReminderRepository) {

    operator fun invoke(): Flow<ClientsState> {
        return repository.loadClientsList()
    }
}
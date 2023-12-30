package com.shv.meetingreminder2.domain.usecases.validation

import com.shv.meetingreminder2.domain.repositories.ReminderValidationRepository
import javax.inject.Inject

class ValidateClientUseCase @Inject constructor(private val repository: ReminderValidationRepository) {

    operator fun invoke(clientName: String): ValidationResult {
        return repository.validateClient(clientName)
    }
}
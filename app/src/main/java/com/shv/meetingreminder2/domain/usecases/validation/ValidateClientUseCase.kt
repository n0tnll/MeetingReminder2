package com.shv.meetingreminder2.domain.usecases.validation

import com.shv.meetingreminder2.domain.repositories.ReminderValidationRepository

class ValidateClientUseCase(private val repository: ReminderValidationRepository) {

    operator fun invoke(clientName: String): ValidationResult {
        return repository.validateClient(clientName)
    }
}
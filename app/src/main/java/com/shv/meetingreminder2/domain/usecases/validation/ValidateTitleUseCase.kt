package com.shv.meetingreminder2.domain.usecases.validation

import com.shv.meetingreminder2.domain.repositories.ReminderValidationRepository

class ValidateTitleUseCase(private val repository: ReminderValidationRepository) {

    operator fun invoke(title: String): ValidationResult {
        return repository.validateTitle(title)
    }
}
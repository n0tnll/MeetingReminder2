package com.shv.meetingreminder2.domain.usecases.validation

import com.shv.meetingreminder2.domain.repositories.ReminderValidationRepository

class ValidateTimeUseCase(private val repository: ReminderValidationRepository) {

    operator fun invoke(date: String, time: String?): ValidationResult {
        return repository.validateDateTime(date, time)
    }
}
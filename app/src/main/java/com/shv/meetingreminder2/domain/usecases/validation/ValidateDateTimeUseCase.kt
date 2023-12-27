package com.shv.meetingreminder2.domain.usecases.validation

import com.shv.meetingreminder2.domain.repositories.ReminderValidationRepository
import java.util.Calendar

class ValidateDateTimeUseCase(private val repository: ReminderValidationRepository) {

    operator fun invoke(calendar: Calendar?): ValidationResult {
        return repository.validateDateTime(calendar)
    }
}
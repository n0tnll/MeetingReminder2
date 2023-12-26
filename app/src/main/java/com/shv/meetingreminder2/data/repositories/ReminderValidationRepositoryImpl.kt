package com.shv.meetingreminder2.data.repositories

import com.shv.meetingreminder2.domain.repositories.ReminderValidationRepository
import com.shv.meetingreminder2.domain.usecases.validation.ValidationResult

class ReminderValidationRepositoryImpl : ReminderValidationRepository {

    override fun validateTitle(title: String): ValidationResult {
        if (title.isBlank()) {
            return ValidationResult(
                successful = false,
                errorMessage = "The title field can't be blank"
            )
        }
        return ValidationResult(
            successful = true
        )
    }

    override fun validateClient(clientName: String): ValidationResult {
        if (clientName.isBlank()) {
            return ValidationResult(
                successful = false,
                errorMessage = "The client must be chosen"
            )
        }
        return ValidationResult(
            successful = true
        )
    }

    override fun validateDate(date: Long): ValidationResult {
        TODO("Not yet implemented")
    }

    override fun validateTime(time: Long): ValidationResult {
        TODO("Not yet implemented")
    }

    private fun validateDateTime(date: Long, time: Long?) {
        time?.let {

        }

    }
}
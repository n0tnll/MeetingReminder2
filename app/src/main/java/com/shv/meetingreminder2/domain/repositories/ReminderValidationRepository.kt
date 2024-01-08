package com.shv.meetingreminder2.domain.repositories

import com.shv.meetingreminder2.domain.usecases.validation.ValidationResult
import java.util.Calendar

interface ReminderValidationRepository {

    fun validateTitle(title: String): ValidationResult

    fun validateClient(clientName: String): ValidationResult

    fun validateDateTime(calendar: Calendar?): ValidationResult
}
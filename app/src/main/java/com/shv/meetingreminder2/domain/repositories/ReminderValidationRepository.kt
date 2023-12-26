package com.shv.meetingreminder2.domain.repositories

import com.shv.meetingreminder2.domain.usecases.validation.ValidationResult

interface ReminderValidationRepository {

    fun validateTitle(title: String): ValidationResult

    fun validateClient(clientName: String): ValidationResult

    fun validateDate(date: Long): ValidationResult

    fun validateTime(time: Long): ValidationResult
}
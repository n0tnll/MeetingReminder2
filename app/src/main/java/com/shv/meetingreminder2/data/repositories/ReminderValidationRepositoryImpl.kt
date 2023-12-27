package com.shv.meetingreminder2.data.repositories

import com.shv.meetingreminder2.domain.repositories.ReminderValidationRepository
import com.shv.meetingreminder2.domain.usecases.validation.ValidationResult
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.util.Calendar

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

    override fun validateDateTime(calendar: Calendar?): ValidationResult {
        if (calendar == null) {
            return ValidationResult(
                successful = true
            )
        } else {
            val currentTimeCalendar = Calendar.getInstance().apply {
                add(Calendar.HOUR_OF_DAY, 1)
            }
            val currentTime = LocalTime.of(
                currentTimeCalendar[Calendar.HOUR_OF_DAY],
                currentTimeCalendar[Calendar.MINUTE],
                0
            )

            val meetingTime = LocalTime.of(
                calendar[Calendar.HOUR_OF_DAY],
                calendar[Calendar.MINUTE],
                0,
            )

            if (checkDateToday(calendar)) {
                if (meetingTime <= currentTime) {
                    return ValidationResult(
                        successful = false,
                        errorMessage = "Incorrect calendar"
                    )
                }
            }

            return ValidationResult(
                successful = true
            )
        }
    }

    private fun checkDateToday(calendar: Calendar): Boolean {
        val meetingDate =
            LocalDateTime.ofInstant(calendar.toInstant(), calendar.timeZone.toZoneId())
                .toLocalDate()
        return meetingDate == LocalDate.now()
    }
}
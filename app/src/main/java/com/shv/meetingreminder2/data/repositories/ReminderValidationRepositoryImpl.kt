package com.shv.meetingreminder2.data.repositories

import com.shv.meetingreminder2.data.extensions.stringDateToCalendar
import com.shv.meetingreminder2.data.extensions.stringTimeToCalendar
import com.shv.meetingreminder2.domain.repositories.ReminderValidationRepository
import com.shv.meetingreminder2.domain.usecases.validation.ValidationResult
import java.time.LocalDate
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

    override fun validateDateTime(date: String, time: String?): ValidationResult {
        if (time.isNullOrBlank()) {
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

            val meetingTimeCalendar = time.stringTimeToCalendar()
            val meetingTime = LocalTime.of(
                meetingTimeCalendar[Calendar.HOUR_OF_DAY],
                meetingTimeCalendar[Calendar.MINUTE],
                0,
            )

            val meetingDateCalendar = date.stringDateToCalendar()

            if (checkDateToday(meetingDateCalendar)) {
                if (meetingTime <= currentTime) {
                    return ValidationResult(
                        successful = false,
                        errorMessage = "Incorrect time"
                    )
                }
            }

            return ValidationResult(
                successful = true
            )
        }
    }

    private fun checkDateToday(calendar: Calendar): Boolean {
        val meetingDate = LocalDate.of(
            calendar[Calendar.YEAR],
            calendar[Calendar.MONTH],
            calendar[Calendar.DAY_OF_MONTH]
        )
        return meetingDate == LocalDate.now()
    }
}
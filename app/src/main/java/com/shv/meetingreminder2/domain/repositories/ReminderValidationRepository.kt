package com.shv.meetingreminder2.domain.repositories

interface ReminderValidationRepository {

    fun validateTitle(title: String)

    fun validateClient(clientName: String)

    fun validateDate(date: Long)

    fun validateTime(time: Long)
}
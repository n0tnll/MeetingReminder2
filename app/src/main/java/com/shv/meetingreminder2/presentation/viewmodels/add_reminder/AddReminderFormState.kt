package com.shv.meetingreminder2.presentation.viewmodels.add_reminder

import java.util.Calendar

data class AddReminderFormState(
    val title: String = "",
    val titleError: String? = null,
    val clientName: String = "",
    val clientNameError: String? = null,
    val date: String = "",
    val time: String? = null,
    val timeError: String? = null,
    val dateTime: Calendar? = null,
    val isFormValid: Boolean = false,
    val isFinished: Boolean = false
)

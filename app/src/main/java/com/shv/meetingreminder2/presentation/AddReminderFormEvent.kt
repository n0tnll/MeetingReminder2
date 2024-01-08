package com.shv.meetingreminder2.presentation

import java.util.Calendar

sealed class AddReminderFormEvent {
    data class TitleChanged(val title: String): AddReminderFormEvent()
    data class ClientChanged(val clientName: String): AddReminderFormEvent()
    data class DateChanged(val date: Calendar): AddReminderFormEvent()
    data class TimeChanged(val time: Calendar?): AddReminderFormEvent()
}

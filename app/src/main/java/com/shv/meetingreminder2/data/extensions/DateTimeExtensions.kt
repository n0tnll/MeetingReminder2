package com.shv.meetingreminder2.data.extensions

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

fun Long.toTimeString(): String {
    val formatter = SimpleDateFormat("HH:mm", Locale.getDefault())
    return formatter.format(this)
}

fun Long.toDateString(): String {
    val formatter = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
    return formatter.format(this)
}

fun String.stringTimeToCalendar(): Calendar {
    val formatter = SimpleDateFormat("HH:mm", Locale.getDefault())
    return Calendar.getInstance().apply {
        time = formatter.parse(this@stringTimeToCalendar) as Date
    }
}

fun String.stringDateToCalendar(): Calendar {
    val formatter = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
    return Calendar.getInstance().apply {
        time = formatter.parse(this@stringDateToCalendar) as Date
    }
}
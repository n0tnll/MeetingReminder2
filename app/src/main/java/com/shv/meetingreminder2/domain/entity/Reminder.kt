package com.shv.meetingreminder2.domain.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.util.concurrent.atomic.AtomicInteger

@Parcelize
data class Reminder(
    val title: String,
    val clientName: String,
    var dateTime: Long,
    var isTimeKnown: Boolean = false,
    var isReminderDone: Boolean = false,
    val client: Client,
    val id: Int = atomicInteger.addAndGet(INCREMENT_REMINDER_ID)
) : Parcelable {
    companion object {
        private val atomicInteger = AtomicInteger(0)
        private const val INCREMENT_REMINDER_ID = 1
    }
}

package com.shv.meetingreminder2.domain.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Reminder(
    val title: String,
    val clientName: String,
    var dateTime: Long,
    var isTimeKnown: Boolean = false,
    var isReminderDone: Boolean = false,
    val client: Client,
    var id: Int = UNDEFINED_ID
) : Parcelable {
    companion object {
        const val UNDEFINED_ID = 0
    }
}

package com.shv.meetingreminder2.domain.entity

data class Reminder(
    val title: String,
    val clientName: String,
    var dataTime: Long,
    var isTimeKnown: Boolean,
    var isReminderDone: Boolean,
    val client: Client,
    var id: Int = UNDEFINED_ID
) {
    companion object {
        private const val UNDEFINED_ID = 0
    }
}

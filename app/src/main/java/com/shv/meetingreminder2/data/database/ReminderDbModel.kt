package com.shv.meetingreminder2.data.database

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.shv.meetingreminder2.domain.entity.Client

@Entity(tableName = "reminders")
data class ReminderDbModel(
    @PrimaryKey(autoGenerate = false)
    val id: Int,
    val title: String,
    var dateTime: Long,
    var isTimeKnown: Boolean,
    var isReminderDone: Boolean,
    @Embedded
    val client: Client
)

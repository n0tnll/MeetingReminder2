package com.shv.meetingreminder2.domain.repositories

import androidx.lifecycle.LiveData
import com.shv.meetingreminder2.domain.entity.Client
import com.shv.meetingreminder2.domain.entity.Reminder

interface MeetingReminderRepository {

    fun getRemindersList(): LiveData<List<Reminder>>

    suspend fun getReminder(id: Int): Reminder

    suspend fun addReminder(reminder: Reminder)

    suspend fun deleteReminder(id: Int)

    suspend fun loadClientsList(): List<Client>

    suspend fun updateAlarmStatus(reminder: Reminder)

    suspend fun getActiveAlarms(time: Long): List<Reminder>
}
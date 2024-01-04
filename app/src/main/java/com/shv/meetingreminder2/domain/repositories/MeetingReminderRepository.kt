package com.shv.meetingreminder2.domain.repositories

import androidx.lifecycle.LiveData
import com.shv.meetingreminder2.domain.entity.Reminder
import com.shv.meetingreminder2.presentation.viewmodels.clients.ClientsState
import kotlinx.coroutines.flow.Flow

interface MeetingReminderRepository {

    fun getRemindersList(): LiveData<List<Reminder>>

    suspend fun getReminder(id: Int): Reminder

    suspend fun addReminder(reminder: Reminder)

    suspend fun deleteReminder(id: Int)

    fun loadClientsList(): Flow<ClientsState>

    suspend fun updateAlarmStatus(reminder: Reminder)

    suspend fun getActiveAlarms(time: Long): List<Reminder>

    suspend fun retryLoadClients()
}
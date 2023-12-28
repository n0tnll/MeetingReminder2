package com.shv.meetingreminder2.data.repositories

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import com.shv.meetingreminder2.data.database.AppDatabase
import com.shv.meetingreminder2.data.mapper.ReminderMapper
import com.shv.meetingreminder2.data.network.api.ApiFactory
import com.shv.meetingreminder2.domain.entity.Client
import com.shv.meetingreminder2.domain.entity.Reminder
import com.shv.meetingreminder2.domain.repositories.MeetingReminderRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MeetingReminderRepositoryImpl(
    private val application: Application
) : MeetingReminderRepository {

    private val reminderDao = AppDatabase.getInstance(application).reminderDao()
    private val apiService = ApiFactory.apiService
    private val mapper = ReminderMapper()

    override fun getRemindersList(): LiveData<List<Reminder>> {
        return reminderDao.getRemindersList().map {
            it.map { dbModel ->
                mapper.mapDbModelToEntity(dbModel)
            }
        }
    }

    override suspend fun getReminder(id: Int): Reminder {
        return mapper.mapDbModelToEntity(reminderDao.getReminder(id))
    }

    override suspend fun addReminder(reminder: Reminder) {
        reminderDao.addReminder(mapper.mapEntityToDbModel(reminder))
    }

    override suspend fun deleteReminder(id: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            reminderDao.deleteReminder(id)
        }
    }

    override suspend fun loadClientsList(): List<Client> {
        val clientsDto = apiService.getClientsList()
        return mapper.mapListDtoToListClient(clientsDto)
    }
}
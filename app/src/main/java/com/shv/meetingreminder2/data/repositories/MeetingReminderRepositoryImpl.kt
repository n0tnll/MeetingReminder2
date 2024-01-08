package com.shv.meetingreminder2.data.repositories

import com.shv.meetingreminder2.data.database.ReminderDao
import com.shv.meetingreminder2.data.database.ReminderDbModel
import com.shv.meetingreminder2.data.mapper.ReminderMapper
import com.shv.meetingreminder2.data.network.api.ApiService
import com.shv.meetingreminder2.domain.entity.Client
import com.shv.meetingreminder2.domain.entity.Reminder
import com.shv.meetingreminder2.domain.repositories.MeetingReminderRepository
import com.shv.meetingreminder2.presentation.viewmodels.clients.ClientsState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.retryWhen
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class MeetingReminderRepositoryImpl @Inject constructor(
    private val reminderDao: ReminderDao,
    private val apiService: ApiService,
    private val mapper: ReminderMapper
) : MeetingReminderRepository {

    private val retryLoadClientEvent = MutableSharedFlow<Unit>()

    override fun getRemindersList(): Flow<List<Reminder>> = flow {
        reminderDao.getRemindersList().map {
            it.map { dbModel ->
                mapper.mapDbModelToEntity(dbModel)
            }
        }.collect {
            emit(it)
        }
    }

    override suspend fun getReminder(id: Int): Reminder {
        return mapper.mapDbModelToEntity(reminderDao.getReminder(id))
    }

    override suspend fun addReminder(reminder: Reminder) {
        CoroutineScope(Dispatchers.IO).launch {
            reminderDao.addReminder(mapper.mapEntityToDbModel(reminder))
        }
    }

    override suspend fun deleteReminder(id: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            reminderDao.deleteReminder(id)
        }
    }

    private suspend fun getClientsFromApi(): List<Client> {
        val clientsDto = apiService.getClientsList()
        return mapper.mapListDtoToListClient(clientsDto)
    }

    override fun loadClientsList(): Flow<ClientsState> = flow {
        emit(getClientsFromApi())
        retryLoadClientEvent.collect {
            emit(getClientsFromApi())
        }
    }.map {
        ClientsState.ClientsList(clientsList = it) as ClientsState
    }.onStart {
        emit(ClientsState.Loading)
    }.retryWhen { cause, _ ->
        emit(ClientsState.LoadingError(cause.message))
        delay(RETRY_TIMEOUT_MILLIS)
        true
    }

    override suspend fun retryLoadClients() {
        retryLoadClientEvent.emit(Unit)
    }

    override suspend fun updateAlarmStatus(reminder: Reminder) {
        CoroutineScope(Dispatchers.IO).launch {
            reminderDao.addReminder(mapper.mapEntityToDbModel(reminder))
        }
    }

    override suspend fun getActiveAlarms(time: Long): List<Reminder> {
        var list: List<ReminderDbModel>
        coroutineScope {
            list = withContext(Dispatchers.IO) {
                reminderDao.getActiveAlarms(time)
            }
        }
        return mapper.mapListReminderDbModelToListEntity(list)
    }

    companion object {
        private const val RETRY_TIMEOUT_MILLIS = 5000L
    }
}
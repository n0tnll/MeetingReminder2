package com.shv.meetingreminder2.data.mapper

import com.shv.meetingreminder2.data.database.ReminderDbModel
import com.shv.meetingreminder2.data.extensions.getFullName
import com.shv.meetingreminder2.data.network.models.ClientDto
import com.shv.meetingreminder2.data.network.models.ClientListDto
import com.shv.meetingreminder2.domain.entity.Client
import com.shv.meetingreminder2.domain.entity.Reminder
import java.util.concurrent.atomic.AtomicInteger

class ReminderMapper {

    fun mapEntityToDbModel(reminder: Reminder): ReminderDbModel {
        return ReminderDbModel(
            id = reminder.id,
            title = reminder.title,
            dataTime = reminder.dateTime,
            isTimeKnown = reminder.isTimeKnown,
            isReminderDone = reminder.isReminderDone,
            client = reminder.client
        )
    }

    fun mapDbModelToEntity(dbModel: ReminderDbModel): Reminder {
        return Reminder(
            id = dbModel.id,
            clientName = dbModel.client.getFullName(),
            title = dbModel.title,
            dateTime = dbModel.dataTime,
            isTimeKnown = dbModel.isTimeKnown,
            isReminderDone = dbModel.isReminderDone,
            client = dbModel.client
        )
    }

    fun mapClientDtoToClient(dto: ClientDto): Client {
        return Client(
            clientId = atomicInteger.addAndGet(INCREMENT_CLIENT_ID),
            accost = dto.fullName.accost,
            firstName = dto.fullName.first,
            lastName = dto.fullName.last,
            email = dto.email,
            imgUrl = dto.imgUrl.large
        )
    }

    fun mapListDtoToListClient(clientListDto: ClientListDto): List<Client> =
        clientListDto.clients.map {
            mapClientDtoToClient(it)
        }

    private companion object {
        private val atomicInteger = AtomicInteger(0)
        private const val INCREMENT_CLIENT_ID = 1
    }
}
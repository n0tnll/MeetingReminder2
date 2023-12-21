package com.shv.meetingreminder2.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import com.shv.meetingreminder2.R
import com.shv.meetingreminder2.data.database.AppDatabase
import com.shv.meetingreminder2.data.extensions.getFullName
import com.shv.meetingreminder2.data.mapper.ReminderMapper
import com.shv.meetingreminder2.data.network.api.ApiFactory
import com.shv.meetingreminder2.domain.entity.Client
import com.shv.meetingreminder2.domain.entity.Reminder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val apiService = ApiFactory.apiService
        val clients = mutableListOf<Client>()
        val mapper = ReminderMapper()
        val dao = AppDatabase.getInstance(this).reminderDao()

        lifecycleScope.launch(Dispatchers.IO) {
           clients.addAll(mapper.mapListDtoToListClient(apiService.getClientsList()))

            val reminder = Reminder(
                title = "pezda",
                clientName = clients[0].getFullName(),
                dataTime = System.currentTimeMillis(),
                isTimeKnown = true,
                isReminderDone = false,
                client = clients[0]
            )

            dao.addReminder(mapper.mapEntityToDbModel(reminder))
        }
    }
}
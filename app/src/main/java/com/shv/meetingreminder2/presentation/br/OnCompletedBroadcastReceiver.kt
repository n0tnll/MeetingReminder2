package com.shv.meetingreminder2.presentation.br

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationManagerCompat
import com.shv.meetingreminder2.data.repositories.MeetingReminderRepositoryImpl
import com.shv.meetingreminder2.domain.entity.Reminder
import com.shv.meetingreminder2.domain.usecases.notifications.UpdateTaskStatusUseCase
import com.shv.meetingreminder2.presentation.AddReminderFragment.Companion.ALARM_RECEIVER_EXTRA
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class OnCompletedBroadcastReceiver : BroadcastReceiver() {

//    private val repository = MeetingReminderRepositoryImpl(application)
//    private val updateTaskStatusUseCase = UpdateTaskStatusUseCase(repository)
    override fun onReceive(context: Context, intent: Intent) {
        val repository = MeetingReminderRepositoryImpl(context.applicationContext)
        val updateTaskStatusUseCase = UpdateTaskStatusUseCase(repository)

        val reminder = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra(
                ALARM_RECEIVER_EXTRA,
                Reminder::class.java
            ) as Reminder
        } else {
            intent.getParcelableExtra<Reminder>(ALARM_RECEIVER_EXTRA)
        }

        reminder?.let {
            it.isReminderDone = true

            CoroutineScope(Dispatchers.IO).launch {
                updateTaskStatusUseCase(it)
            }

            NotificationManagerCompat.from(context).cancel(null, it.id)
        }
    }

    companion object {
        fun newIntent(context: Context): Intent {
            return Intent(context, OnCompletedBroadcastReceiver::class.java)
        }
    }
}
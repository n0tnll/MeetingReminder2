package com.shv.meetingreminder2.presentation.br

import android.app.AlarmManager
import android.app.Application
import android.app.PendingIntent
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
import java.util.Calendar

class OnRepeatableBroadcastReceiver(
    application: Application
) : BroadcastReceiver() {

    private val repository = MeetingReminderRepositoryImpl(application)
    private val updateTaskStatusUseCase = UpdateTaskStatusUseCase(repository)

    override fun onReceive(context: Context, intent: Intent) {
        val reminder = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra(
                ALARM_RECEIVER_EXTRA,
                Reminder::class.java
            ) as Reminder
        } else {
            intent.getParcelableExtra<Reminder>(ALARM_RECEIVER_EXTRA)
        }

        reminder?.let {
            NotificationManagerCompat.from(context).cancel(null, it.id)
            val newMeetingTime = Calendar.getInstance().apply {
                timeInMillis = it.dateTime
                add(Calendar.HOUR_OF_DAY, 1)
            }
            it.dateTime = newMeetingTime.timeInMillis

            CoroutineScope(Dispatchers.IO).launch {
                updateTaskStatusUseCase(it)
            }
            updateNotification(reminder, context)
        }
    }

    private fun updateNotification(reminder: Reminder, context: Context) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = AlarmReceiver.newIntent(context).apply {
            putExtra(ALARM_RECEIVER_EXTRA, reminder)
        }
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            reminder.id,
            intent,
            PendingIntent.FLAG_IMMUTABLE
        )
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, reminder.dateTime, pendingIntent) //TODO(request permission)
    }

    companion object {
        fun newIntent(context: Context): Intent {
            return Intent(context, OnRepeatableBroadcastReceiver::class.java)
        }
    }
}
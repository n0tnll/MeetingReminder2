package com.shv.meetingreminder2.presentation.br

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationManagerCompat
import com.shv.meetingreminder2.MeetingReminderApplication
import com.shv.meetingreminder2.domain.entity.Reminder
import com.shv.meetingreminder2.domain.usecases.notifications.UpdateTaskStatusUseCase
import com.shv.meetingreminder2.presentation.AddReminderFragment.Companion.ALARM_RECEIVER_EXTRA
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Calendar
import javax.inject.Inject

class OnRepeatableBroadcastReceiver : BroadcastReceiver() {

    @Inject
    lateinit var updateTaskStatusUseCase: UpdateTaskStatusUseCase

    override fun onReceive(context: Context, intent: Intent) {
        (context.applicationContext as MeetingReminderApplication).component.inject(this)
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
                add(Calendar.HOUR_OF_DAY, ADD_ONE_HOUR_TO_TIME)
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
        alarmManager.setExact(
            AlarmManager.RTC_WAKEUP,
            reminder.dateTime,
            pendingIntent
        )
    }

    companion object {

        private const val ADD_ONE_HOUR_TO_TIME = 1

        fun newIntent(context: Context): Intent {
            return Intent(context, OnRepeatableBroadcastReceiver::class.java)
        }
    }
}
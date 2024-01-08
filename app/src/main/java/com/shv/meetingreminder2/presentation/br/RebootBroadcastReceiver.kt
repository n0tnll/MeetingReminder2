package com.shv.meetingreminder2.presentation.br

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.shv.meetingreminder2.MeetingReminderApplication
import com.shv.meetingreminder2.domain.entity.Reminder
import com.shv.meetingreminder2.domain.usecases.notifications.GetActiveAlarmsUseCase
import com.shv.meetingreminder2.presentation.AddReminderFragment.Companion.ALARM_RECEIVER_EXTRA
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Calendar
import javax.inject.Inject

class RebootBroadcastReceiver : BroadcastReceiver() {

    @Inject
    lateinit var getActiveAlarmsUseCase: GetActiveAlarmsUseCase
    override fun onReceive(context: Context, intent: Intent) {
        (context.applicationContext as MeetingReminderApplication).component.inject(this)
        if (intent.action.equals("android.intent.action.BOOT_COMPLETED")) {
            val time = Calendar.getInstance().timeInMillis
            CoroutineScope(Dispatchers.Main).launch {
                val list = getActiveAlarmsUseCase(time)
                for (reminder in list) setAlarm(reminder, context)
            }
        }
    }

    private fun setAlarm(reminder: Reminder, context: Context?) {
        val alarmManager = context?.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = AlarmReceiver.newIntent(context).apply {
            putExtra(ALARM_RECEIVER_EXTRA, reminder)
        }

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            reminder.id,
            intent,
            PendingIntent.FLAG_IMMUTABLE
        )
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, reminder.dateTime, pendingIntent)
    }
}
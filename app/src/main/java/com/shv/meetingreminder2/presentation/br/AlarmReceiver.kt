package com.shv.meetingreminder2.presentation.br

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat.getSystemService
import com.shv.meetingreminder2.R
import com.shv.meetingreminder2.data.extensions.toTimeString
import com.shv.meetingreminder2.domain.entity.Reminder
import com.shv.meetingreminder2.presentation.AddReminderFragment.Companion.ALARM_RECEIVER_EXTRA

class AlarmReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val reminder = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra(ALARM_RECEIVER_EXTRA, Reminder::class.java) as Reminder
        } else {
            intent.getParcelableExtra(ALARM_RECEIVER_EXTRA) as? Reminder
        }

        val notificationManager =
            getSystemService(context, NotificationManager::class.java) as NotificationManager

        createNotificationChannel(
            context,
            notificationManager,
            reminder?.isTimeKnown ?: throw RuntimeException("reminder is null")
        )

        val intentCompleted = OnCompletedBroadcastReceiver.newIntent(context).apply {
            putExtra(ALARM_RECEIVER_EXTRA, reminder)
        }
        val pendingIntentCompeted = reminder.let {
            PendingIntent.getBroadcast(
                context,
                it.id,
                intentCompleted,
                PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
            )
        }
        val actionComplete = NotificationCompat.Action.Builder(
            null,
            context.getString(R.string.ok_action),
            pendingIntentCompeted
        ).build()

        val intentRepeatable = OnRepeatableBroadcastReceiver.newIntent(context).apply {
            putExtra(ALARM_RECEIVER_EXTRA, reminder)
        }
        val pendingIntentRepeatable = reminder.let {
            PendingIntent.getBroadcast(
                context,
                it.id,
                intentRepeatable,
                PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
            )
        }
        val actionRepeat = NotificationCompat.Action.Builder(
            null,
            context.getString(R.string.repeat_about_1_hour_action),
            pendingIntentRepeatable
        ).build()

        val notificationWhenTimeKnown = NotificationCompat.Builder(context, CHANNEL_TIME_KNOWN_ID)
            .setContentTitle(reminder.title)
            .setContentText(
                String.format(
                    context.getString(R.string.content_notification_known_time),
                    reminder.clientName,
                    reminder.dateTime.toTimeString()
                )
            )
            .setSmallIcon(R.drawable.ic_meeting_notification)
            .setPriority(NotificationCompat.PRIORITY_MAX)
            .setContentIntent(pendingIntentCompeted)
            .addAction(actionComplete)
            .build()

        val notificationWhenTimeUnknown = NotificationCompat.Builder(context, CHANNEL_REPEATABLE_ID)
            .setContentTitle(reminder.title)
            .setContentText(
                String.format(
                    context.getString(R.string.content_notification_unknown_time),
                    reminder.clientName
                )
            )
            .setSmallIcon(R.drawable.ic_meeting_notification)
            .setPriority(NotificationCompat.PRIORITY_MAX)
            .setContentIntent(pendingIntentRepeatable)
            .addAction(actionRepeat)
            .addAction(actionComplete)
            .setAutoCancel(true)
            .build()

        if (reminder.isTimeKnown)
            notificationManager.notify(reminder.id, notificationWhenTimeKnown)
        else
            notificationManager.notify(reminder.id, notificationWhenTimeUnknown)
    }

    private fun createNotificationChannel(
        context: Context,
        notificationManager: NotificationManager,
        isTimeKnown: Boolean
    ) {
        val channel = NotificationChannel(
            if (isTimeKnown) CHANNEL_TIME_KNOWN_ID else CHANNEL_REPEATABLE_ID,
            if (isTimeKnown) CHANNEL_TIME_KNOWN_NAME else CHANNEL_REPEATABLE_NAME,
            NotificationManager.IMPORTANCE_HIGH
        ).apply {
            description =
                if (isTimeKnown)
                    context.getString(R.string.notification_about_meeting_with_client_alarm_rec)
                else
                    context.getString(R.string.notification_without_current_meeting_time_alarm_rec)
        }
        notificationManager.createNotificationChannel(channel)
    }

    companion object {
        private const val CHANNEL_TIME_KNOWN_ID = "channel_time_known"
        private const val CHANNEL_TIME_KNOWN_NAME = "Single notification"

        private const val CHANNEL_REPEATABLE_ID = "channel_repeatable"
        private const val CHANNEL_REPEATABLE_NAME = "Repeatable notification"

        fun newIntent(context: Context): Intent {
            return Intent(context, AlarmReceiver::class.java)
        }
    }
}
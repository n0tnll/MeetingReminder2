package com.shv.meetingreminder2.presentation.adapters.reminders

import android.content.Context
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.shv.meetingreminder2.domain.entity.Reminder

class RemindersAdapter : ListAdapter<Reminder, RemindersViewHolder>(ReminderDiffUtil()) {

    private lateinit var context: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RemindersViewHolder {
        context = parent.context
        return when (viewType) {
            REMINDER_COMPLETED -> {
                RemindersViewHolder.ReminderCompletedViewHolder.from(parent)
            }

            else -> RemindersViewHolder.ReminderScheduledViewHolder.from(parent)
        }
    }

    override fun onBindViewHolder(holder: RemindersViewHolder, position: Int) {
        val reminder = getItem(position)
        when (holder.itemViewType) {
            REMINDER_COMPLETED -> {
                (holder as RemindersViewHolder.ReminderCompletedViewHolder)
                    .bind(reminder, context)
            }

            REMINDER_SCHEDULED -> {
                (holder as RemindersViewHolder.ReminderScheduledViewHolder)
                    .bind(reminder, context)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position).isReminderDone) {
            true -> REMINDER_COMPLETED
            else -> REMINDER_SCHEDULED
        }

    }

    companion object {
        private const val REMINDER_COMPLETED = 101
        private const val REMINDER_SCHEDULED = 102
    }
}
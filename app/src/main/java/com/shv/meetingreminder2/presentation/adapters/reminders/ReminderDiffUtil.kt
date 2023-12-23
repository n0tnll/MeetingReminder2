package com.shv.meetingreminder2.presentation.adapters.reminders

import androidx.recyclerview.widget.DiffUtil
import com.shv.meetingreminder2.domain.entity.Reminder

class ReminderDiffUtil : DiffUtil.ItemCallback<Reminder>() {
    override fun areItemsTheSame(oldItem: Reminder, newItem: Reminder): Boolean {
        return newItem.id == oldItem.id
    }

    override fun areContentsTheSame(oldItem: Reminder, newItem: Reminder): Boolean {
        return newItem == oldItem
    }
}
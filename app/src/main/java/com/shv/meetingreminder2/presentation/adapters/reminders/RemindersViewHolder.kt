package com.shv.meetingreminder2.presentation.adapters.reminders

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import coil.load
import com.shv.meetingreminder2.data.extensions.toDateString
import com.shv.meetingreminder2.data.extensions.toTimeString
import com.shv.meetingreminder2.databinding.ReminderItemCompletedBinding
import com.shv.meetingreminder2.databinding.ReminderItemScheduledBinding
import com.shv.meetingreminder2.domain.entity.Reminder

sealed class RemindersViewHolder(
    binding: ViewBinding
) : RecyclerView.ViewHolder(binding.root) {

    class ReminderScheduledViewHolder(
        private val binding: ReminderItemScheduledBinding
    ) : RemindersViewHolder(binding) {

        fun bind(reminder: Reminder, context: Context) {
            with(binding) {
                with(reminder) {
                    tvReminderTitle.text = title
                    tvClientFullName.text = clientName
                    tvEmail.text = client.email
                    tvReminderTime.text =
                        if (isTimeKnown) dateTime.toTimeString() else "During the day"
                    tvReminderDate.text = dateTime.toDateString()
                    ivClientPhoto.load(client.imgUrl)
                }
            }
        }

        companion object {
            fun from(parent: ViewGroup): ReminderScheduledViewHolder {
                val binding = ReminderItemScheduledBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                return ReminderScheduledViewHolder(binding)
            }
        }
    }

    class ReminderCompletedViewHolder(
        private val binding: ReminderItemCompletedBinding
    ) : RemindersViewHolder(binding) {

        fun bind(reminder: Reminder, context: Context) {
            with(binding) {
                with(reminder) {
                    tvReminderTitle.text = title
                    tvClientFullName.text = clientName
                    tvEmail.text = client.email
                    tvReminderTime.text =
                        if (isTimeKnown) dateTime.toTimeString() else "During the day"
                    tvReminderDate.text = dateTime.toDateString()
                    ivClientPhoto.load(client.imgUrl)
                }
            }
        }

        companion object {

            fun from(parent: ViewGroup): ReminderCompletedViewHolder {
                val binding = ReminderItemCompletedBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )

                return ReminderCompletedViewHolder(binding)
            }
        }
    }
}
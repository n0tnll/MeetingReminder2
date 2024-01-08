package com.shv.meetingreminder2

import android.app.Application
import com.shv.meetingreminder2.di.DaggerApplicationComponent

class MeetingReminderApplication : Application() {

    val component by lazy {
        DaggerApplicationComponent.factory()
            .create(this)
    }
}
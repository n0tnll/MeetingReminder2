package com.shv.meetingreminder2.di.modules

import android.app.Application
import com.shv.meetingreminder2.data.database.AppDatabase
import com.shv.meetingreminder2.data.database.ReminderDao
import com.shv.meetingreminder2.data.network.api.ApiFactory
import com.shv.meetingreminder2.data.network.api.ApiService
import dagger.Module
import dagger.Provides

@Module
interface DataModule {

    companion object {

        @Provides
        fun provideApiService(): ApiService {
            return ApiFactory.apiService
        }

        @Provides
        fun provideReminderDao(
            application: Application
        ): ReminderDao {
            return AppDatabase.getInstance(application).reminderDao()
        }
    }
}
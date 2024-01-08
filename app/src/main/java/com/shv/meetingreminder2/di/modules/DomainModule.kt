package com.shv.meetingreminder2.di.modules

import com.shv.meetingreminder2.data.repositories.MeetingReminderRepositoryImpl
import com.shv.meetingreminder2.data.repositories.ReminderValidationRepositoryImpl
import com.shv.meetingreminder2.di.ApplicationScope
import com.shv.meetingreminder2.domain.repositories.MeetingReminderRepository
import com.shv.meetingreminder2.domain.repositories.ReminderValidationRepository
import dagger.Binds
import dagger.Module

@Module
interface DomainModule {

    @ApplicationScope
    @Binds
    fun bindMeetingReminderRepository(impl: MeetingReminderRepositoryImpl): MeetingReminderRepository

    @Binds
    fun bindReminderValidationRepository(impl: ReminderValidationRepositoryImpl): ReminderValidationRepository
}
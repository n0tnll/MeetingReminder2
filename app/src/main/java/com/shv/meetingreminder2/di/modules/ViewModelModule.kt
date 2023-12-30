package com.shv.meetingreminder2.di.modules

import androidx.lifecycle.ViewModel
import com.shv.meetingreminder2.presentation.viewmodels.add_reminder.AddReminderViewModel
import com.shv.meetingreminder2.presentation.viewmodels.clients.ClientsViewModel
import com.shv.meetingreminder2.presentation.viewmodels.reminders.RemindersViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
interface ViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(RemindersViewModel::class)
    fun bindRemindersViewModel(viewModel: RemindersViewModel) : ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(AddReminderViewModel::class)
    fun bindAddReminderViewModel(viewModel: AddReminderViewModel) : ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ClientsViewModel::class)
    fun bindClientsViewModel(viewModel: ClientsViewModel) : ViewModel
}
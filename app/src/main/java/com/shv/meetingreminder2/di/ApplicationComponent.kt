package com.shv.meetingreminder2.di

import android.app.Application
import com.shv.meetingreminder2.di.modules.DataModule
import com.shv.meetingreminder2.di.modules.DomainModule
import com.shv.meetingreminder2.di.modules.ViewModelModule
import com.shv.meetingreminder2.presentation.AddReminderFragment
import com.shv.meetingreminder2.presentation.ClientsListFragment
import com.shv.meetingreminder2.presentation.RemindersListFragment
import com.shv.meetingreminder2.presentation.br.OnCompletedBroadcastReceiver
import com.shv.meetingreminder2.presentation.br.OnRepeatableBroadcastReceiver
import com.shv.meetingreminder2.presentation.br.RebootBroadcastReceiver
import dagger.BindsInstance
import dagger.Component

@ApplicationScope
@Component(
    modules = [
        DomainModule::class,
        DataModule::class,
        ViewModelModule::class
    ]
)
interface ApplicationComponent {

    fun inject(remindersListFragment: RemindersListFragment)

    fun inject(addReminderFragment: AddReminderFragment)

    fun inject(clientsListFragment: ClientsListFragment)

    fun inject(br: OnCompletedBroadcastReceiver)

    fun inject(br: OnRepeatableBroadcastReceiver)

    fun inject(br: RebootBroadcastReceiver)

    @Component.Factory
    interface ApplicationComponentFactory {

        fun create(
            @BindsInstance application: Application
        ): ApplicationComponent
    }
}
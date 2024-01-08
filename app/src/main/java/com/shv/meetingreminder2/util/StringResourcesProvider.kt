package com.shv.meetingreminder2.util

import android.app.Application
import androidx.annotation.StringRes
import com.shv.meetingreminder2.di.ApplicationScope
import javax.inject.Inject

@ApplicationScope
class StringResourcesProvider @Inject constructor(
    private val application: Application
){
    fun getString(@StringRes stringResId: Int): String {
        return application.getString(stringResId)
    }
}
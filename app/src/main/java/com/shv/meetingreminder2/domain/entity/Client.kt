package com.shv.meetingreminder2.domain.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Client(
    val clientId: Int,
    val accost: String,
    val firstName: String,
    val lastName: String,
    val email: String,
    val imgUrl: String
) : Parcelable

package com.shv.meetingreminder2.data.extensions

import com.shv.meetingreminder2.domain.entity.Client

fun Client.getFullName(): String {
    with(this) {
        return String.format("%s. %s %s", accost, firstName, lastName)
    }
}
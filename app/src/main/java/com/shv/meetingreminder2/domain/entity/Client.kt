package com.shv.meetingreminder2.domain.entity

data class Client(
    val clientId: Int,
    val accost: String,
    val firstName: String,
    val lastName: String,
    val email: String,
    val imgUrl: String
)

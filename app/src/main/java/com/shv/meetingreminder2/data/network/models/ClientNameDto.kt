package com.shv.meetingreminder2.data.network.models


import com.google.gson.annotations.SerializedName

data class ClientNameDto(
    @SerializedName("first")
    val first: String,
    @SerializedName("last")
    val last: String,
    @SerializedName("title")
    val accost: String
)
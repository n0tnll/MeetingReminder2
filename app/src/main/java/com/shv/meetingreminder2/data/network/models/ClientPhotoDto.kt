package com.shv.meetingreminder2.data.network.models


import com.google.gson.annotations.SerializedName

data class ClientPhotoDto(
    @SerializedName("large")
    val large: String,
    @SerializedName("medium")
    val medium: String,
    @SerializedName("thumbnail")
    val thumbnail: String
)
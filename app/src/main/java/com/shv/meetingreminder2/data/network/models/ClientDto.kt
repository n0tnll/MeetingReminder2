package com.shv.meetingreminder2.data.network.models


import com.google.gson.annotations.SerializedName

data class ClientDto(
    @SerializedName("email")
    val email: String,
    @SerializedName("name")
    val fullName: ClientNameDto,
    @SerializedName("picture")
    val imgUrl: ClientPhotoDto
)
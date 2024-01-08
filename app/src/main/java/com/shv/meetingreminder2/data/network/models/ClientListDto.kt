package com.shv.meetingreminder2.data.network.models


import com.google.gson.annotations.SerializedName

data class ClientListDto(
    @SerializedName("results")
    val clients: List<ClientDto>
)
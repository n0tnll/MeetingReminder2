package com.shv.meetingreminder2.presentation.viewmodels.clients

import com.shv.meetingreminder2.domain.entity.Client

sealed class ClientsState {
    data object Loading : ClientsState()

    data class LoadingError(
        val errorMessage: String? = null
    ) : ClientsState()

    data class ClientsList(
        val clientsList: List<Client> = arrayListOf()
    ) : ClientsState()
}

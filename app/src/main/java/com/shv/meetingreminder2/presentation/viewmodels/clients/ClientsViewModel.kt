package com.shv.meetingreminder2.presentation.viewmodels.clients

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shv.meetingreminder2.data.extensions.mergeWith
import com.shv.meetingreminder2.domain.usecases.LoadClientsListUseCase
import com.shv.meetingreminder2.domain.usecases.RetryLoadClientsUseCase
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class ClientsViewModel @Inject constructor(
    private val loadClientsListUseCase: LoadClientsListUseCase,
    private val retryLoadClientsUseCase: RetryLoadClientsUseCase
) : ViewModel() {

    private val loadingFlow = MutableSharedFlow<ClientsState>()
    val state = loadClientsListUseCase()
        .mergeWith(loadingFlow)

    fun retryLoadClients() {
        viewModelScope.launch {
            loadingFlow.emit(ClientsState.Loading)
            retryLoadClientsUseCase()
        }
    }
}
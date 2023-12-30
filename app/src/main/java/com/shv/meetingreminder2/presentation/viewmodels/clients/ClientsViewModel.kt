package com.shv.meetingreminder2.presentation.viewmodels.clients

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shv.meetingreminder2.domain.usecases.LoadClientsListUseCase
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import javax.inject.Inject

class ClientsViewModel @Inject constructor(
    private val loadClientsListUseCase: LoadClientsListUseCase
) : ViewModel() {

    private val _state = MutableLiveData<ClientsState>()
    val state: LiveData<ClientsState>
        get() = _state

    init {
        loadClients()
    }

    fun loadClients() {
        val clients = viewModelScope.async {
            _state.value = Loading
            loadClientsListUseCase()
        }
        viewModelScope.launch {
            try {
                _state.value = ClientsList(
                    clientsList = clients.await()
                )
            } catch (e: Exception) {
                _state.value = LoadingError(
                    errorMessage = e.message
                )
            }
        }
    }
}
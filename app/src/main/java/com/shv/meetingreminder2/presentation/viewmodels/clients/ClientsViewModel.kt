package com.shv.meetingreminder2.presentation.viewmodels.clients

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.shv.meetingreminder2.data.MeetingReminderRepositoryImpl
import com.shv.meetingreminder2.domain.usecases.LoadClientsListUseCase
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class ClientsViewModel(
    application: Application
) : AndroidViewModel(application) {

    private val repository = MeetingReminderRepositoryImpl(application)
    private val loadClientsListUseCase = LoadClientsListUseCase(repository)

    private val _state = MutableLiveData<ClientsState>()
    val state: LiveData<ClientsState>
        get() = _state

    init {
        loadClients()
    }

    private fun loadClients() {
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
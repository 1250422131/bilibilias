package com.imcys.bilibilias.ui.event.requestFrequent

import androidx.compose.runtime.mutableFloatStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed interface RequestFrequentUIState {
    object Success : RequestFrequentUIState
    object Default : RequestFrequentUIState
    object Loading : RequestFrequentUIState
}


class RequestFrequentViewModel(
    private val httpClient: HttpClient,
) : ViewModel() {

    private val _uiState = MutableStateFlow<RequestFrequentUIState>(RequestFrequentUIState.Default)
    val uiState = _uiState.asStateFlow()

    fun retryRequest(url: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _uiState.value = RequestFrequentUIState.Loading
            runCatching {
                httpClient.get(url)
            }.onSuccess {
                if (it.status.value == 200) {
                    _uiState.value = RequestFrequentUIState.Success
                } else {
                    _uiState.value = RequestFrequentUIState.Default
                }
            }.onFailure {
                _uiState.value = RequestFrequentUIState.Default
            }
        }
    }
}
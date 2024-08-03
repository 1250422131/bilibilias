package com.imcys.bilibilias

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject
import kotlin.time.Duration.Companion.seconds

@HiltViewModel
class MainActivityViewModel @Inject constructor() : ViewModel() {
    val uiState: StateFlow<MainActivityUiState> = flow {
        delay(1.seconds)
        emit(MainActivityUiState.Success)
    }
        .stateIn(
            scope = viewModelScope,
            initialValue = MainActivityUiState.Loading,
            started = SharingStarted.WhileSubscribed(5_000),
        )
}

sealed interface MainActivityUiState {
    data object Loading : MainActivityUiState
    data object Success : MainActivityUiState
}
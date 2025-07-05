package com.imcys.bilibilias.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.imcys.bilibilias.datastore.source.UsersDataSource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class BILIBILIASAppViewModel(
    private val usersDataSource: UsersDataSource
) : ViewModel() {

    sealed class UIState {
        data object DEFAULT : UIState()
        data class ACCOUNTCHECK(
            val isCheckLoading: Boolean = false  // 改为 val
        ) : UIState()
    }

    private val _uiState = MutableStateFlow<UIState>(UIState.DEFAULT)
    val uiState = _uiState.asStateFlow()

    fun accountLoginStateError() {
        viewModelScope.launch {
            _uiState.emit(UIState.ACCOUNTCHECK(true))
        }
    }

    fun updateUIState(uiState: UIState) {
        viewModelScope.launch {
            _uiState.emit(uiState)
        }
    }
}
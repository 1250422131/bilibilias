package com.imcys.bilibilias

import androidx.compose.runtime.Composable
import com.imcys.bilibilias.core.common.molecule.BaseViewModel
import com.imcys.bilibilias.core.data.toast.ToastMachine
import com.imcys.bilibilias.core.data.util.NetworkMonitor
import com.imcys.bilibilias.core.model.data.UserData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    val networkMonitor: NetworkMonitor,
    val toastMachine: ToastMachine,
) : BaseViewModel<Unit, MainActivityUiState>() {
    @Composable
    override fun models(events: Flow<Unit>): MainActivityUiState {
        return MainActivityUiState.Loading
    }
}

sealed interface MainActivityUiState {
    data object Loading : MainActivityUiState
    data class Success(val userData: UserData) : MainActivityUiState
}

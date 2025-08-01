package com.imcys.bilibilias.logic.login

import com.imcys.bilibilias.core.logging.logger
import com.imcys.bilibilias.logic.root.AppComponentContext
import com.imcys.bilibilias.logic.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

interface LoginComponent {
    val uiState: StateFlow<LoginResultUiState>
    val qrCodeUrl: StateFlow<String>
    fun dispatch(action: LoginAction)
}

class DefaultLoginComponent(
    componentContext: AppComponentContext,
    stateMachine: LoginStateMachine,
) : LoginComponent, AppComponentContext by componentContext {
    private val machine = stateMachine.launchIn(viewModelScope)
    override val uiState = MutableStateFlow<LoginResultUiState>(LoginResultUiState.Loading)
    override val qrCodeUrl = MutableStateFlow("")

    init {
        lifecycle.subscribe(stateMachine)
        viewModelScope.launch {
            machine.state.collect { state ->
                when (state) {
                    is LoginState.LoginFailed -> {
                        uiState.update { LoginResultUiState.Error(state.message) }
                    }

                    is LoginState.GeneratingQrCode -> {}
                    is LoginState.LoginSuccessful -> {
                        uiState.update { LoginResultUiState.Success }
                    }

                    is LoginState.AwaitingConfirmation -> {}
                    is LoginState.QrCodeReady -> {
                        qrCodeUrl.update { state.qrCodeUrl }
                    }
                }
            }
        }
    }

    override fun dispatch(action: LoginAction) {
        viewModelScope.launch {
            machine.dispatch(action)
        }
    }

    companion object {
        private val logger = logger<LoginComponent>()
    }
}

@Deprecated("Use LoginState")
sealed interface LoginResultUiState {
    data class Error(val message: String? = null) : LoginResultUiState
    data object Loading : LoginResultUiState
    data object Success : LoginResultUiState
}
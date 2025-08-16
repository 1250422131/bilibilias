package com.imcys.bilibilias.logic.login

import com.imcys.bilibilias.core.logging.logger
import com.imcys.bilibilias.logic.root.AppComponentContext
import com.imcys.bilibilias.logic.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

interface LoginComponent {
    val cookieStateMachine: CookieStateMachine
    val uiState: StateFlow<LoginResultUiState>
    val qrCodeUrl: StateFlow<String>
    fun dispatch(action: LoginAction)
}

class DefaultLoginComponent(
    componentContext: AppComponentContext,
    stateMachine: LoginStateMachine,
    override val cookieStateMachine: CookieStateMachine,
) : LoginComponent, AppComponentContext by componentContext {
    private val machine = stateMachine.launchIn(viewModelScope)
    override val uiState = MutableStateFlow<LoginResultUiState>(LoginResultUiState.Loading)
    override val qrCodeUrl = MutableStateFlow("")

    init {
        lifecycle.subscribe(stateMachine)
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
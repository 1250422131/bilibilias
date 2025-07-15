package com.imcys.bilibilias.logic.login

import co.touchlab.kermit.Logger
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.essenty.lifecycle.doOnDestroy
import com.imcys.bilibilias.core.datasource.api.BilibiliLoginApi
import com.imcys.bilibilias.core.datasource.persistent.TokenPersistent
import com.imcys.bilibilias.logic.utils.scope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeout
import kotlin.time.Duration.Companion.seconds

interface LoginComponent {
    val uiState: StateFlow<LoginResultUiState>
    val qrCodeUrl: StateFlow<String>
    suspend fun initiateLogin()                  // Action to start the login process
}

class DefaultLoginComponent(
    componentContext: ComponentContext
) : LoginComponent, ComponentContext by componentContext {
    override val uiState = MutableStateFlow<LoginResultUiState>(LoginResultUiState.Loading)
    override val qrCodeUrl = MutableStateFlow("")

    init {
        lifecycle.doOnDestroy {
            BilibiliLoginApi.close()
        }
    }

    override suspend fun initiateLogin() {
        val qrCode = BilibiliLoginApi.getQrcode()
        pollForLoginStatus(qrCode.qrcodeKey)
        qrCodeUrl.update { qrCode.url }
    }

    private fun pollForLoginStatus(key: String) {
        scope.launch(Dispatchers.IO) {
            try {
                withTimeout(180.seconds) {
                    var loginSuccessful = false
                    while (!loginSuccessful && isActive) {
                        val response = BilibiliLoginApi.pollRequest(key)
                        loginSuccessful = response.code == 0
                        if (loginSuccessful) {
                            uiState.update { LoginResultUiState.Success }
                            TokenPersistent.setRefreshToken(response.refreshToken)
                            Logger.i { "Login success" }
                        } else {
                            delay(1.seconds)
                        }
                    }

                    if (!loginSuccessful && isActive) {
                        Logger.i { "Login polling finished without success (not timed out)." }
                    }
                }
            } catch (e: TimeoutCancellationException) {
                uiState.update { LoginResultUiState.Error(e.message) }
                Logger.i(e) { "Login timed out after 180 seconds." }
            } catch (e: Exception) {
                uiState.update { LoginResultUiState.Error(e.message) }
                Logger.i(e) { "An error occurred during login: ${e.message}" }
            }
        }
    }
}

sealed interface LoginResultUiState {
    data class Error(val message: String? = null) : LoginResultUiState
    data object Loading : LoginResultUiState
    data object Success : LoginResultUiState
}
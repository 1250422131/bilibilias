package com.imcys.bilibilias.logic.login

import com.imcys.bilibilias.core.datasource.api.BilibiliLoginApi
import com.imcys.bilibilias.core.datasource.persistent.TokenPersistent
import com.imcys.bilibilias.logic.root.AppComponentContext
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
    componentContext: AppComponentContext,
    private val tokenPersistent: TokenPersistent,
) : LoginComponent, AppComponentContext by componentContext {
    override val uiState = MutableStateFlow<LoginResultUiState>(LoginResultUiState.Loading)
    override val qrCodeUrl = MutableStateFlow("")

    override suspend fun initiateLogin() {
        val qrCode = BilibiliLoginApi.getQrcode()
        pollForLoginStatus(qrCode.qrcodeKey)
        qrCodeUrl.update { qrCode.url }
    }

    private fun pollForLoginStatus(key: String) {
        backgroundScope.launch(Dispatchers.IO) {
            try {
                withTimeout(180.seconds) {
                    var loginSuccessful = false
                    while (!loginSuccessful && isActive) {
                        val response = BilibiliLoginApi.pollRequest(key)
                        loginSuccessful = response.code == 0
                        if (loginSuccessful) {
                            uiState.update { LoginResultUiState.Success }
                            tokenPersistent.setRefreshToken(response.refreshToken)
                            logger.i { "Login success" }
                        } else {
                            delay(1.seconds)
                        }
                    }

                    if (!loginSuccessful && isActive) {
                        logger.i { "Login polling finished without success (not timed out)." }
                    }
                }
            } catch (e: TimeoutCancellationException) {
                uiState.update { LoginResultUiState.Error(e.message) }
                logger.i(e) { "Login timed out after 180 seconds." }
            } catch (e: Exception) {
                uiState.update { LoginResultUiState.Error(e.message) }
                logger.i(e) { "An error occurred during login: ${e.message}" }
            }
        }
    }

    override fun onDestroy() {
        BilibiliLoginApi.close()
    }
}

sealed interface LoginResultUiState {
    data class Error(val message: String? = null) : LoginResultUiState
    data object Loading : LoginResultUiState
    data object Success : LoginResultUiState
}
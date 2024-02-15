package com.imcys.authentication

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.imcys.datastore.fastkv.PersistentCookie
import com.imcys.network.repository.auth.IAuthDataSources
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.flow.updateAndGet
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeout
import javax.inject.Inject
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Duration.Companion.seconds


@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: IAuthDataSources,
    private val persistentCookie: PersistentCookie
) : ViewModel() {
    private val _loginAuthState = MutableStateFlow(LoginAuthState())
    val loginAuthUiState = _loginAuthState.asStateFlow()

    init {
        getQRCode()
    }
    fun getQRCode() {
        viewModelScope.launch(Dispatchers.IO) {
            val (key, url) = authRepository.获取二维码()
            _loginAuthState.update { it.copy(qrCodeUrl = url + "main-fe-header") }
            tryLogin(key)
        }
    }

    /**
     * 0：扫码登录成功 86038：二维码已失效 86090：二维码已扫码未确认 86101：未扫码
     */
    private suspend fun tryLogin(key: String) {
        withTimeout(3.minutes) {
            var ok = false
            while (isActive && !ok) {
                delay(1.seconds)
                val response = authRepository.轮询登录接口(key)
                val state = _loginAuthState.updateAndGet {
                    it.copy(
                        qrCodeMessage = response.message,
                        isSuccess = response.isSuccess,
                        snackBarMessage = if (response.isSuccess) "登录成功" else null
                    )
                }
                persistentCookie.setRefreshToke(response.refreshToken)
                ok = state.isSuccess
            }
        }
    }
}

data class LoginAuthState(
    val qrCodeUrl: String = "",

    val qrCodeMessage: String = "",
    val isSuccess: Boolean = false,

    val snackBarMessage: String? = null,
)

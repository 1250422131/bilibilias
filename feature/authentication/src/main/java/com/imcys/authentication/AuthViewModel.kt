package com.imcys.authentication

import androidx.lifecycle.*
import com.imcys.datastore.fastkv.*
import com.imcys.network.repository.auth.*
import dagger.hilt.android.lifecycle.*
import io.github.aakira.napier.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import javax.inject.*
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Duration.Companion.seconds


@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: IAuthDataSources,
) : ViewModel() {
    private val _loginAuthState = MutableStateFlow(LoginAuthState())
    val loginAuthUiState = _loginAuthState.asStateFlow()
    private var job: Job? = null

    init {
        getQRCode()
    }

    fun getQRCode() {
        job?.cancel()
        job = viewModelScope.launch(Dispatchers.IO) {
            val (key, url) = authRepository.获取二维码()
            _loginAuthState.update { it.copy(qrCodeUrl = url) }
            tryLogin(key)
        }
        PersistentCookie.save()
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
                Napier.d { "是否登录 ${PersistentCookie.logging}, ${PersistentCookie.getCookie()}" }
                val state = _loginAuthState.updateAndGet {
                    it.copy(
                        qrCodeMessage = response.message,
                        isSuccess = PersistentCookie.logging,
                        snackBarMessage = if (response.isSuccess) "登录成功" else null
                    )
                }
                PersistentCookie.setRefreshToke(response.refreshToken)
                ok = PersistentCookie.logging
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

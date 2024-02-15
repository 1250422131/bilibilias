package com.imcys.home

import androidx.lifecycle.*
import com.imcys.datastore.fastkv.*
import com.imcys.network.repository.auth.*
import com.imcys.network.repository.wbi.*
import dagger.hilt.android.lifecycle.*
import kotlinx.coroutines.*
import javax.inject.*

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val authRepository: IAuthDataSources,
    private val cookieStorage: CookieStorage,
    private val wbiKeyStorage: WbiKeyStorage,
    private val wbiKeyRepository: IWbiSignatureDataSources
) : ViewModel() {
    init {
        requestWbiKey()
    }

    private fun requestWbiKey() {
        viewModelScope.launch {
            val mixKey = wbiKeyRepository.getSignature()
            wbiKeyStorage.save(mixKey)
        }
    }

    fun logout() {
        viewModelScope.launch {
            authRepository.退出登录()
            cookieStorage.clear()
        }
    }
}

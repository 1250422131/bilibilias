package com.imcys.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.imcys.datastore.fastkv.CookieStorage
import com.imcys.datastore.fastkv.WbiKeyStorage
import com.imcys.network.repository.auth.IAuthDataSources
import com.imcys.network.repository.wbi.IWbiSignatureDataSources
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

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
            wbiKeyStorage.updateLocalDate()
            if (wbiKeyStorage.shouldUpdate()) {
                val mixKey = wbiKeyRepository.getSignature()
                wbiKeyStorage.save(mixKey)
                wbiKeyStorage.updateLocalDate()
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            authRepository.退出登录()
            cookieStorage.clear()
        }
    }
}

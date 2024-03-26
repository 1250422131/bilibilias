package com.imcys.bilibilias.splash.ui

import androidx.lifecycle.ViewModel
import com.imcys.bilibilias.core.datastore.LoginInfoDataSource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(private val loginInfoDataSource: LoginInfoDataSource) :
    ViewModel() {
    internal val isLogin = runBlocking { loginInfoDataSource.loginState.first() }
}

package com.imcys.bilibilias.ui.splash

import androidx.lifecycle.ViewModel
import com.imcys.datastore.fastkv.CookieStorage
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(cookieStorage: CookieStorage) : ViewModel() {
    val valid = cookieStorage.logging
}

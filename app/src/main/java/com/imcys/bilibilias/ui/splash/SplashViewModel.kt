package com.imcys.bilibilias.ui.splash

import androidx.lifecycle.ViewModel
import com.imcys.datastore.fastkv.CookiesData
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(cookiesData: CookiesData) : ViewModel() {
    val expired = cookiesData.isExpired
}

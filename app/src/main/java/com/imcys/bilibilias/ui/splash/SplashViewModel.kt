package com.imcys.bilibilias.ui.splash

import androidx.lifecycle.*
import com.imcys.datastore.datastore.*
import dagger.hilt.android.lifecycle.*
import javax.inject.*

@HiltViewModel
class SplashViewModel @Inject constructor(cookieDataSource: CookieDataSource) : ViewModel() {
    val valid = cookieDataSource.loginState
}

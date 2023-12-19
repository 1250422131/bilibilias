package com.imcys.bilibilias.ui.splash

import androidx.lifecycle.ViewModel
import com.imcys.datastore.fastkv.ICookieStore
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(cookiesData: ICookieStore) : ViewModel() {
    val valid = cookiesData.valid
}

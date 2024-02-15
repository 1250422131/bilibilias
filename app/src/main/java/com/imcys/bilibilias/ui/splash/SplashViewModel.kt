package com.imcys.bilibilias.ui.splash

import androidx.lifecycle.ViewModel
import com.imcys.datastore.fastkv.PersistentCookie
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(persistentCookie: PersistentCookie) : ViewModel() {
    val valid = persistentCookie.logging
}

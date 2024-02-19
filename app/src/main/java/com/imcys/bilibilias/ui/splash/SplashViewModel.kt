package com.imcys.bilibilias.ui.splash

import androidx.lifecycle.*
import com.imcys.datastore.fastkv.*
import dagger.hilt.android.lifecycle.*
import javax.inject.*

@HiltViewModel
class SplashViewModel @Inject constructor() : ViewModel() {
    val valid = PersistentCookie.logging
}

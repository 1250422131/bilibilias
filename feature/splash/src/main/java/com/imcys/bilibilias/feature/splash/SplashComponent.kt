package com.imcys.bilibilias.feature.splash

import com.arkivanov.decompose.ComponentContext
import com.imcys.bilibilias.core.datastore.login.LoginInfoDataSource
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

class SplashComponent @AssistedInject constructor(
    @Assisted componentContext: ComponentContext,
    private val loginInfoDataSource: LoginInfoDataSource
) : ComponentContext by componentContext {
    val isLogin: Boolean = runBlocking { loginInfoDataSource.loginState.first() }

    @AssistedFactory
    interface Factory {
        operator fun invoke(
            componentContext: ComponentContext,
        ): SplashComponent
    }
}

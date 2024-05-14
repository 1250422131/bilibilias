package com.imcys.bilibilias.feature.splash

import com.arkivanov.decompose.ComponentContext
import com.imcys.bilibilias.core.datastore.login.LoginInfoDataSource
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

class DefaultSplashComponent @AssistedInject constructor(
    @Assisted componentContext: ComponentContext,
    private val loginInfoDataSource: LoginInfoDataSource
) : SplashComponent, ComponentContext by componentContext {
    override val isLogin: Boolean = runBlocking { loginInfoDataSource.loginState.first() }

    @AssistedFactory
    interface Factory : SplashComponent.Factory {
        override fun invoke(
            componentContext: ComponentContext,
        ): DefaultSplashComponent
    }
}

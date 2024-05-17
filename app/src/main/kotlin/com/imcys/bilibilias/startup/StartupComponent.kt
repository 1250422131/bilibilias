package com.imcys.bilibilias.startup

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.value.Value
import com.imcys.bilibilias.feature.login.LoginComponent
import com.imcys.bilibilias.feature.splash.SplashComponent
import com.imcys.bilibilias.navigation.RootComponent

interface StartupComponent {
    val stack: Value<ChildStack<*, Child>>
    fun onLoginClicked()
    fun onRootClicked()
    sealed class Child {
        data class SplashChild(val component: SplashComponent) : Child()
        data class LoginChild(val component: LoginComponent) : Child()
        data class RootChild(val component: RootComponent) : Child()
    }

    fun interface Factory {
        operator fun invoke(
            componentContext: ComponentContext,
        ): StartupComponent
    }
}

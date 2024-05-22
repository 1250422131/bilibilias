package com.imcys.bilibilias.feature.splash

import com.arkivanov.decompose.ComponentContext

interface SplashComponent {
    val isLogin: Boolean

    interface Factory {
        operator fun invoke(
            componentContext: ComponentContext,
        ): SplashComponent
    }
}

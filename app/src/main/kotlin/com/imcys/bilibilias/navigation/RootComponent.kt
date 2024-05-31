package com.imcys.bilibilias.navigation

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.value.Value
import com.imcys.bilibilias.core.model.video.ViewInfo
import com.imcys.bilibilias.feature.download.component.DownloadComponent
import com.imcys.bilibilias.feature.home.HomeComponent
import com.imcys.bilibilias.feature.login.LoginComponent
import com.imcys.bilibilias.feature.player.component.PlayerComponent
import com.imcys.bilibilias.feature.splash.SplashComponent
import com.imcys.bilibilias.feature.tool.ToolComponent

interface RootComponent {

    val stack: Value<ChildStack<*, Child>>
    val currentDestination: RootComponent.Child
    val currentTopLevelDestination: TopLevelDestination?

    fun onHomeTabClicked()
    fun onToolTabClicked()
    fun onDownloadTabClicked()
    fun onPlayedTabClicked(viewInfo: ViewInfo)
    fun onLoginTabClicked()
    sealed class Child {
        data class HomeChild(val component: HomeComponent) : Child()

        data class ToolChild(val component: ToolComponent) : Child()

        data class DownloadChild(val component: DownloadComponent) : Child()

        data class PlayerChild(val component: PlayerComponent) : Child()

        data object UserChild : Child()
        data class SplashChild(val component: SplashComponent) : Child()
        data class LoginChild(val component: LoginComponent) : Child()
    }

    interface Factory {
        operator fun invoke(
            componentContext: ComponentContext,
        ): RootComponent
    }
}

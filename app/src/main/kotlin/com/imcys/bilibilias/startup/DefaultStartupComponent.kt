package com.imcys.bilibilias.startup

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.push
import com.arkivanov.decompose.router.stack.replaceAll
import com.arkivanov.decompose.value.Value
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject

class DefaultStartupComponent @AssistedInject constructor(
    @Assisted componentContext: ComponentContext,
    private val splashFactory: SplashComponent.Factory,
) : StartupComponent, ComponentContext by componentContext {
    private val navigation = StackNavigation<Config>()
    override val stack: Value<ChildStack<*, StartupComponent.Child>> =
        childStack(
            source = navigation,
            serializer = null,
            initialConfiguration = Config.Splash,
            handleBackButton = true,
            childFactory = ::child,
        )

    override fun onLoginClicked() {
        navigation.push(Config.Login)
    }

    override fun onRootClicked() {
        navigation.replaceAll(Config.Root)
    }

    private fun child(config: Config, componentContext: ComponentContext): StartupComponent.Child =
        when (config) {
            Config.Splash -> StartupComponent.Child.SplashChild(splashFactory(componentContext))
            Config.Login -> StartupComponent.Child.LoginChild
            Config.Root -> StartupComponent.Child.RootChild
        }

    private sealed interface Config {
        data object Splash : Config
        data object Login : Config
        data object Root : Config
    }

    @AssistedFactory
    interface Factory : StartupComponent.Factory {
        override fun invoke(
            componentContext: ComponentContext,
        ): DefaultStartupComponent
    }
}

package com.imcys.bilibilias.navigation

import androidx.tracing.trace
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.active
import com.arkivanov.decompose.router.stack.bringToFront
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.push
import com.arkivanov.decompose.value.Value
import com.imbys.bilibilias.feature.authorspace.AuthorSpaceComponent
import com.imcys.bilibilias.core.model.download.FileType
import com.imcys.bilibilias.core.model.video.Mid
import com.imcys.bilibilias.core.model.video.ViewInfo
import com.imcys.bilibilias.feature.download.component.DownloadComponent
import com.imcys.bilibilias.feature.home.HomeComponent
import com.imcys.bilibilias.feature.login.LoginComponent
import com.imcys.bilibilias.feature.player.component.PlayerComponent
import com.imcys.bilibilias.feature.settings.SettingsComponent
import com.imcys.bilibilias.feature.splash.SplashComponent
import com.imcys.bilibilias.feature.tool.ToolComponent
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import io.github.aakira.napier.Napier
import kotlinx.serialization.Serializable

@Suppress("LongParameterList")
class DefaultRootComponent @AssistedInject constructor(
    @Assisted componentContext: ComponentContext,
    private val homeComponentFactory: HomeComponent.Factory,
    private val toolComponentFactory: ToolComponent.Factory,
    private val downloadComponentFactory: DownloadComponent.Factory,
    private val playerComponentFactory: PlayerComponent.Factory,
    private val splashComponentFactory: SplashComponent.Factory,
    private val loginComponentFactory: LoginComponent.Factory,
    private val settingsComponentFactory: SettingsComponent.Factory,
    private val authorSpaceComponentFactory: AuthorSpaceComponent.Factory
) : RootComponent, ComponentContext by componentContext {

    private val navigation = StackNavigation<Config>()
    override val shouldShowBottomBar: Boolean
        get() = when (currentDestination) {
            is RootComponent.Child.HomeChild,
            is RootComponent.Child.ToolChild,
            is RootComponent.Child.DownloadChild -> true

            else -> false
        }
    override val currentDestination: RootComponent.Child
        get() = stack.active.instance

    override val currentTopLevelDestination: TopLevelDestination?
        get() {
            var active: TopLevelDestination? = null
            stack.subscribe {
                Napier.d { "TopLevelDestination" + it.toString() }
                active = when (it.active.instance) {
                    is RootComponent.Child.HomeChild -> TopLevelDestination.HOME
                    is RootComponent.Child.ToolChild -> TopLevelDestination.TOOL
                    is RootComponent.Child.DownloadChild -> TopLevelDestination.DOWNLOAD
                    else -> null
                }
            }
            return active
        }

    override val stack: Value<ChildStack<*, RootComponent.Child>> =
        childStack(
            source = navigation,
            serializer = Config.serializer(),
            initialConfiguration = Config.Splash,
            handleBackButton = true,
            childFactory = ::child,
        )

    override fun onHomeTabClicked() {
        navigation.bringToFront(Config.Home)
    }

    override fun onToolTabClicked() {
        navigation.bringToFront(Config.Tool)
    }

    override fun onDownloadTabClicked() {
        navigation.bringToFront(Config.Download)
    }

    override fun onPlayedTabClicked(viewInfo: ViewInfo) {
        navigation.bringToFront(Config.Player(viewInfo))
    }

    override fun onLoginTabClicked() {
        navigation.push(Config.Login)
    }

    override fun onSettingsTabClicked() {
        navigation.push(Config.Settings)
    }

    override fun onAuthorSpaceTabClicked(mid: Mid) {
        navigation.push(Config.AuthorSpace(mid))
    }

    private fun child(config: Config, componentContext: ComponentContext): RootComponent.Child =
        trace("Navigation: $config") {
            when (config) {
                is Config.User -> error("未实现用户页面")
                is Config.Home -> RootComponent.Child.HomeChild(
                    homeComponentFactory(componentContext)
                )

                is Config.Tool -> RootComponent.Child.ToolChild(
                    toolComponentFactory(componentContext)
                )

                is Config.Download -> RootComponent.Child.DownloadChild(
                    downloadComponentFactory(componentContext)
                )

                is Config.Player -> RootComponent.Child.PlayerChild(
                    playerComponentFactory(
                        componentContext,
                        config.info,
                        FileType.VIDEO
                    )
                )

                Config.Login -> RootComponent.Child.LoginChild(
                    loginComponentFactory(componentContext)
                )

                Config.Splash -> RootComponent.Child.SplashChild(
                    splashComponentFactory(componentContext)
                )

                Config.Settings -> RootComponent.Child.SettingsChild(
                    settingsComponentFactory(componentContext)
                )

                is Config.AuthorSpace -> RootComponent.Child.AuthorSpaceChild(
                    authorSpaceComponentFactory(componentContext, config.mid)
                )
            }
        }

    @Serializable
    private sealed interface Config {
        @Serializable
        data object Home : Config

        @Serializable
        data object Tool : Config

        @Serializable
        data object Download : Config

        @Serializable
        data object User : Config

        @Serializable
        data class Player(val info: ViewInfo) : Config

        @Serializable
        data object Splash : Config

        @Serializable
        data object Login : Config

        @Serializable
        data object Settings : Config

        @Serializable
        data class AuthorSpace(val mid: Mid) : Config
    }

    @AssistedFactory
    interface Factory : RootComponent.Factory {
        override fun invoke(
            componentContext: ComponentContext,
        ): DefaultRootComponent
    }
}

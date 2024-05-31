package com.imcys.bilibilias.navigation

import androidx.tracing.trace
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.active
import com.arkivanov.decompose.router.stack.bringToFront
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.value.Value
import com.imcys.bilibilias.core.model.download.FileType
import com.imcys.bilibilias.core.model.video.ViewInfo
import com.imcys.bilibilias.feature.download.component.DownloadComponent
import com.imcys.bilibilias.feature.home.HomeComponent
import com.imcys.bilibilias.feature.player.component.PlayerComponent
import com.imcys.bilibilias.feature.tool.ToolComponent
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import io.github.aakira.napier.Napier
import kotlinx.serialization.Serializable

class DefaultRootComponent @AssistedInject constructor(
    @Assisted componentContext: ComponentContext,
    private val homeComponentFactory: HomeComponent.Factory,
    private val toolComponentFactory: ToolComponent.Factory,
    private val downloadComponentFactory: DownloadComponent.Factory,
    private val playerComponentFactory: PlayerComponent.Factory
) : RootComponent, ComponentContext by componentContext {

    private val navigation = StackNavigation<Config>()

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
            initialConfiguration = Config.Tool,
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

    private fun child(config: Config, componentContext: ComponentContext): RootComponent.Child =
        trace("Navigation: $config") {
            when (config) {
                is Config.Home -> RootComponent.Child.HomeChild(
                    homeComponentFactory(
                        componentContext
                    )
                )

                is Config.Tool -> RootComponent.Child.ToolChild(
                    toolComponentFactory(
                        componentContext
                    )
                )

                is Config.Download -> RootComponent.Child.DownloadChild(
                    downloadComponentFactory(
                        componentContext
                    )
                )

                Config.User -> error("")
                is Config.Player -> RootComponent.Child.PlayerChild(
                    playerComponentFactory(
                        componentContext,
                        config.info,
                        FileType.VIDEO
                    )
                )
            }
        }

    @Serializable
    private sealed interface Config {
        data object Home : Config
        data object Tool : Config
        data object Download : Config
        data object User : Config
        data class Player(val info: ViewInfo) : Config
    }

    @AssistedFactory
    interface Factory : RootComponent.Factory {
        override fun invoke(
            componentContext: ComponentContext,
        ): DefaultRootComponent
    }
}

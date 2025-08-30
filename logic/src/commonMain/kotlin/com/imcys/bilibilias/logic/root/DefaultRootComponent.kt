package com.imcys.bilibilias.logic.root

import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.bringToFront
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.pop
import com.arkivanov.decompose.router.stack.pushNew
import com.arkivanov.decompose.value.Value
import com.imcys.bilibilias.logic.cache.DefaultCacheComponent
import com.imcys.bilibilias.logic.login.DefaultLoginComponent
import com.imcys.bilibilias.logic.player.DefaultPlayerComponent
import com.imcys.bilibilias.logic.root.RootComponent.Child.CacheChild
import com.imcys.bilibilias.logic.root.RootComponent.Child.LoginChild
import com.imcys.bilibilias.logic.root.RootComponent.Child.PlayerChild
import com.imcys.bilibilias.logic.root.RootComponent.Child.SearchChild
import com.imcys.bilibilias.logic.root.RootComponent.Child.SettingsChild
import com.imcys.bilibilias.logic.search.DefaultSearchComponent
import com.imcys.bilibilias.logic.setting.DefaultSettingsComponent
import kotlinx.serialization.Serializable
import org.koin.core.component.get
import org.koin.core.qualifier.named

class DefaultRootComponent(
    componentContext: AppComponentContext,
    private val searchText: String?,
) : RootComponent, AppComponentContext by componentContext {
    private val nav = StackNavigation<Config>()

    private val _stack = childStack(
        source = nav,
        serializer = Config.serializer(),
        childFactory = ::child,
        initialConfiguration = Config.Search(searchText),
    )

    override val stack: Value<ChildStack<*, RootComponent.Child>> = _stack

    private fun child(config: Config, context: AppComponentContext): RootComponent.Child =
        when (config) {
            is Config.Search -> SearchChild(
                DefaultSearchComponent(
                    context,
                    config.text,
                    get(),
                    get(),
                    get(),
                    get(),
                    get(),
                )
            )

            Config.Cache -> CacheChild(DefaultCacheComponent(context, get(named("ffmpeg"))))
            Config.Login -> LoginChild(DefaultLoginComponent(context, get(), get(), get(), get()))
            Config.Player -> PlayerChild(DefaultPlayerComponent(context))
            Config.Settings -> SettingsChild(DefaultSettingsComponent(context, get()))
        }

    override fun onSettingsClicked() {
        nav.pushNew(Config.Settings)
    }

    override fun onPlayerClicked() {
        nav.pushNew(Config.Player)
    }

    override fun onBackClicked() {
        nav.pop()
    }

    override fun onSearchClicked() {
        nav.bringToFront(Config.Search(searchText))
    }

    override fun onCacheClicked() {
        nav.bringToFront(Config.Cache)
    }

    override fun onLoginClicked() {
        nav.pushNew(Config.Login)
    }

    @Serializable
    private sealed interface Config {
        @Serializable
        data class Search(val text: String?) : Config

        @Serializable
        data object Cache : Config

        @Serializable
        data object Login : Config

        @Serializable
        data object Player : Config

        @Serializable
        data object Settings : Config
    }
}
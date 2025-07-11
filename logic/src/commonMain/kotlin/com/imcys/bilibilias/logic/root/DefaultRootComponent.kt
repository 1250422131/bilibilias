package com.imcys.bilibilias.logic.root

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.bringToFront
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.pop
import com.arkivanov.decompose.value.Value
import com.imcys.bilibilias.logic.cache.DefaultCacheComponent
import com.imcys.bilibilias.logic.search.DefaultSearchComponent
import kotlinx.serialization.Serializable

class DefaultRootComponent(
    componentContext: ComponentContext,
) : RootComponent, ComponentContext by componentContext {

    private val nav = StackNavigation<Config>()

    private val _stack = childStack(
            source = nav,
            serializer = Config.serializer(),
            childFactory = ::child,
            initialConfiguration = Config.Search,
        )

    override val stack: Value<ChildStack<*, RootComponent.Child>> = _stack

    private fun child(config: Config, componentContext: ComponentContext): RootComponent.Child =
        when (config) {
            Config.Search -> RootComponent.Child.SearchChild(DefaultSearchComponent(componentContext))
            Config.Cache -> RootComponent.Child.SearchChild(DefaultCacheComponent(componentContext))
        }

    override fun onBackClicked() {
        nav.pop()
    }

    override fun onSearchClicked() {
        nav.bringToFront(Config.Search)
    }

    override fun onCacheClicked() {
        nav.bringToFront(Config.Cache)
    }

    @Serializable
    private sealed interface Config {
        @Serializable
        data object Search : Config

        @Serializable
        data object Cache : Config
    }
}
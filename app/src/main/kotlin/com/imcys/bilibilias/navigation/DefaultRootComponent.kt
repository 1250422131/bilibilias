package com.imcys.bilibilias.navigation

import androidx.tracing.trace
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.bringToFront
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.value.Value

class DefaultRootComponent(
    componentContext: ComponentContext,
) : RootComponent, ComponentContext by componentContext {

    private val navigation = StackNavigation<Config>()

    override val stack: Value<ChildStack<*, RootComponent.Child>> =
        childStack(
            source = navigation,
            serializer = null,
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

    private fun child(config: Config, componentContext: ComponentContext): RootComponent.Child =
        trace("Navigation: $config") {
            when (config) {
                Config.Home -> RootComponent.Child.HomeChild
                Config.Tool -> RootComponent.Child.ToolChild
                Config.Download -> RootComponent.Child.DownloadChild
                Config.User -> error("")
            }
        }

    //    private fun listComponent(componentContext: ComponentContext): ListComponent =
//        DefaultListComponent(
//            componentContext = componentContext,
//            onItemSelected = { item: String -> // Supply dependencies and callbacks
//                navigation.push(Config.Details(item = item)) // Push the details component
//            },
//        )
    private sealed interface Config {
        data object Home : Config
        data object Tool : Config
        data object Download : Config
        data object User : Config
    }
}

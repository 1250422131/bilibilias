package com.imcys.bilibilias.navigation

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.tracing.trace
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.hashString
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.bringToFront
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.value.Value
import com.imcys.bilibilias.R

interface RootComponent {

    val stack: Value<ChildStack<*, Child>>

    fun onHomeTabClicked()
    fun onToolTabClicked()
    fun onDownloadTabClicked()

    sealed class Child {
        @get:StringRes
        abstract val title: Int

        @get:DrawableRes
        abstract val selectedIcon: Int

        @get:DrawableRes
        abstract val unselectedIcon: Int

        data object HomeChild : Child() {
            override val selectedIcon = R.drawable.ic_home_home_true
            override val unselectedIcon = R.drawable.ic_home_home
            override val title = R.string.app_home_menu_button_navigation_home
        }

        data object ToolChild : Child() {
            override val selectedIcon = R.drawable.ic_home_tool_true
            override val unselectedIcon = R.drawable.ic_home_tool
            override val title = R.string.app_home_menu_button_navigation_tool
        }

        data object DownloadChild : Child() {
            override val selectedIcon = R.drawable.ic_home_download_four_true
            override val unselectedIcon = R.drawable.ic_home_download_four
            override val title = R.string.app_home_menu_button_navigation_download
        }

        data object UserChild : Child() {
            override val selectedIcon = R.drawable.ic_home_people_true
            override val unselectedIcon = R.drawable.ic_home_people
            override val title = R.string.app_home_menu_button_navigation_user
        }
    }
}

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
        trace("Navigation: ${config}") {
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

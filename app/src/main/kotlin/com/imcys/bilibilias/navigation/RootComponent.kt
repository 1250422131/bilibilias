package com.imcys.bilibilias.navigation

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.arkivanov.decompose.router.stack.ChildStack
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

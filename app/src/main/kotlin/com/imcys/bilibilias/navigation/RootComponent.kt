package com.imcys.bilibilias.navigation

import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.value.Value
import com.imcys.bilibilias.feature.home.HomeComponent

interface RootComponent {

    val stack: Value<ChildStack<*, Child>>

    fun onHomeTabClicked()
    fun onToolTabClicked()
    fun onDownloadTabClicked()

    sealed class Child {
        data class HomeChild(val component: HomeComponent) : Child()

        data object ToolChild : Child()

        data object DownloadChild : Child()

        data object UserChild : Child()
    }
}

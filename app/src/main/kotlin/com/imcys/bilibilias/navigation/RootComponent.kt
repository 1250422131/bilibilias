package com.imcys.bilibilias.navigation

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.value.Value
import com.imcys.bilibilias.feature.download.DownloadComponent
import com.imcys.bilibilias.feature.home.HomeComponent
import com.imcys.bilibilias.feature.tool.ToolComponent

interface RootComponent {

    val stack: Value<ChildStack<*, Child>>

    fun onHomeTabClicked()
    fun onToolTabClicked()
    fun onDownloadTabClicked()

    sealed class Child {
        data class HomeChild(val component: HomeComponent) : Child()

        data class ToolChild(val component: ToolComponent) : Child()

        data class DownloadChild(val component: DownloadComponent) : Child()

        data object UserChild : Child()
    }
    interface Factory {
        operator fun invoke(
            componentContext: ComponentContext,
        ): RootComponent
    }
}

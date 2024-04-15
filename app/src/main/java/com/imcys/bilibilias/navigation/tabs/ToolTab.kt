package com.imcys.bilibilias.navigation.tabs

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import cafe.adriel.voyager.navigator.tab.TabOptions
import com.imcys.bilibilias.feature.tool.ToolRoute
import com.imcys.bilibilias.navigation.TopLevelDestination

object ToolTab : TabX() {

    override val options: TabOptions
        @Composable
        get() {
            val title = stringResource(TopLevelDestination.Tool.iconTextId)
            val icon = TabIcon(this, TopLevelDestination.Tool)
            return remember {
                TabOptions(
                    index = 1u,
                    title = title,
                    icon = icon
                )
            }
        }

    @Composable
    override fun Content(modifier: Modifier) {
        ToolRoute(modifier)
    }
}

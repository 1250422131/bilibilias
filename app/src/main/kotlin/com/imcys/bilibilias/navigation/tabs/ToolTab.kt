package com.imcys.bilibilias.navigation.tabs

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.res.stringResource
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
import com.imcys.bilibilias.core.player.PlayerScreen
import com.imcys.bilibilias.feature.settings.SettingScreen
import com.imcys.bilibilias.feature.tool.ToolRoute
import com.imcys.bilibilias.navigation.TopLevelDestination

object ToolTab : Tab {

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
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        ToolRoute(
            onSetting = { navigator.parent?.push(SettingScreen) },
            onPlayer = { /*navigator.parent?.push(PlayerScreen)*/ }
        )
    }
}

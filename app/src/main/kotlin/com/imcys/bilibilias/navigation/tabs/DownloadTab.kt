package com.imcys.bilibilias.navigation.tabs

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.res.stringResource
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
import com.imcys.bilibilias.core.player.PlayerScreen
import com.imcys.bilibilias.feature.download.DownloadRoute
import com.imcys.bilibilias.navigation.TopLevelDestination

object DownloadTab : Tab {

    override val options: TabOptions
        @Composable
        get() {
            val title = stringResource(TopLevelDestination.Download.iconTextId)
            val icon = TabIcon(this, TopLevelDestination.Download)
            return remember {
                TabOptions(
                    index = 2u,
                    title = title,
                    icon = icon
                )
            }
        }

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        DownloadRoute(
            onPlayer = { vUri, aUri -> navigator.parent?.push(PlayerScreen(vUri, aUri)) }
        )
    }
}

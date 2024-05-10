package com.imcys.bilibilias.navigation.tabs

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.res.stringResource
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
import com.imcys.bilibilias.feature.download.DownloadRoute1
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
        with(DownloadRoute1){
            Content()
        }
    }
}

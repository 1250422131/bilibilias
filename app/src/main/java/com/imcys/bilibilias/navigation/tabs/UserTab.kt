package com.imcys.bilibilias.navigation.tabs

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import cafe.adriel.voyager.navigator.tab.TabOptions
import com.imcys.bilibilias.feature.user.UserRoute
import com.imcys.bilibilias.navigation.TopLevelDestination

object UserTab : TabX() {

    override val options: TabOptions
        @Composable
        get() {
            val title = stringResource(TopLevelDestination.User.iconTextId)
            val icon = TabIcon(this, TopLevelDestination.User)
            return remember {
                TabOptions(
                    index = 3u,
                    title = title,
                    icon = icon
                )
            }
        }

    @Composable
    override fun Content(modifier: Modifier) {
        UserRoute(modifier)
    }
}

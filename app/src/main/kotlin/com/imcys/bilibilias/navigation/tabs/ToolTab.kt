package com.imcys.bilibilias.navigation.tabs

import android.content.Intent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
import com.imcys.bilibilias.feature.tool.ToolRoute
import com.imcys.bilibilias.home.ui.activity.SettingActivity
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
        val context = LocalContext.current
        ToolRoute {
            val intent = Intent(context, SettingActivity::class.java)
            context.startActivity(intent)
        }
    }
}

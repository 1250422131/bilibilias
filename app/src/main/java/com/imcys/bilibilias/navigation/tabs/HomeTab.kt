package com.imcys.bilibilias.navigation.tabs

import android.content.Intent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import cafe.adriel.voyager.navigator.tab.TabOptions
import com.imcys.bilibilias.feature.home.HomeRoute
import com.imcys.bilibilias.home.ui.activity.DedicateActivity
import com.imcys.bilibilias.home.ui.activity.DonateActivity
import com.imcys.bilibilias.navigation.TopLevelDestination

object HomeTab : TabX() {

    override val options: TabOptions
        @Composable
        get() {
            val title = stringResource(TopLevelDestination.Home.iconTextId)
            val icon = TabIcon(this, TopLevelDestination.Home)
            return remember {
                TabOptions(
                    index = 0u,
                    title = title,
                    icon = icon
                )
            }
        }

    @Composable
    override fun Content(modifier: Modifier) {
        val context = LocalContext.current
        HomeRoute(
            modifier = Modifier,
            onSalute = {
                val intent = Intent(context, DedicateActivity::class.java)
                context.startActivity(intent)
            },
            onDonation = {
                val intent = Intent(context, DonateActivity::class.java)
                context.startActivity(intent)
            }
        )
    }
}

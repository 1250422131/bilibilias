package com.imcys.bilibilias.ui.download.danmaku

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.imcys.bilibilias.base.utils.sharedHiltViewModel
import com.imcys.bilibilias.ui.player.PlayerViewModel

const val ROUTE_DANMAKU = "danmaku"
fun NavController.navigateToDanmaku() {
    navigate(ROUTE_DANMAKU)
}

fun NavGraphBuilder.danmakuRoute(
    navController: NavHostController,
    onBack: () -> Unit
) = composable(ROUTE_DANMAKU) { backStackEntry ->
    val viewModel: PlayerViewModel = backStackEntry.sharedHiltViewModel(navController)
    val state by viewModel.playerState.collectAsStateWithLifecycle()
    DanmakuRoute()
}

@Composable
fun DanmakuRoute() {
    DanmakuScreen()
}

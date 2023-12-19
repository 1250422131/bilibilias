package com.imcys.player.download.danmaku

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.google.accompanist.navigation.material.ExperimentalMaterialNavigationApi
import com.imcys.common.utils.sharedHiltViewModel
import com.imcys.player.PlayerState
import com.imcys.player.PlayerViewModel
import com.imcys.player.download.DownloadViewModel

const val ROUTE_DANMAKU = "danmaku"
fun NavController.navigateToDanmaku() {
    navigate(ROUTE_DANMAKU)
}

@OptIn(ExperimentalMaterialNavigationApi::class)
fun NavGraphBuilder.danmakuRoute(
    navController: NavHostController,
    onBack: () -> Unit
) = composable(ROUTE_DANMAKU) { backStackEntry ->
    val viewModel: PlayerViewModel = backStackEntry.sharedHiltViewModel(navController)
    val downloadViewModel: DownloadViewModel = backStackEntry.sharedHiltViewModel(navController)
    val state by viewModel.playerState.collectAsStateWithLifecycle()
    DanmakuRoute(state, downloadViewModel::downloadDanmaku, state.videoDetails.aid)
}

@Composable
fun DanmakuRoute(state: PlayerState, downloadDanmaku: (String, Long) -> Unit, aid: Long) {
    DanmakuScreen(state.videoDetails.pageData, downloadDanmaku, aid)
}

package com.imcys.bilibilias.ui.download.danmaku

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import com.google.accompanist.navigation.material.ExperimentalMaterialNavigationApi
import com.google.accompanist.navigation.material.bottomSheet
import com.imcys.bilibilias.base.utils.sharedHiltViewModel
import com.imcys.bilibilias.ui.download.DownloadViewModel
import com.imcys.bilibilias.ui.play.PlayerState
import com.imcys.bilibilias.ui.play.PlayerViewModel

const val ROUTE_DANMAKU = "danmaku"
fun NavController.navigateToDanmaku() {
    navigate(ROUTE_DANMAKU)
}

@OptIn(ExperimentalMaterialNavigationApi::class)
fun NavGraphBuilder.danmakuRoute(
    navController: NavHostController,
    onBack: () -> Unit
) = bottomSheet(ROUTE_DANMAKU) { backStackEntry ->
    val viewModel: PlayerViewModel = backStackEntry.sharedHiltViewModel(navController)
    val downloadViewModel: DownloadViewModel = backStackEntry.sharedHiltViewModel(navController)
    val state by viewModel.playerState.collectAsStateWithLifecycle()
    DanmakuRoute(state, downloadViewModel::downloadDanmaku, state.videoDetails.aid)
}

@Composable
fun DanmakuRoute(state: PlayerState, downloadDanmaku: (Long, Long) -> Unit, aid: Long) {
    DanmakuScreen(state.videoDetails.pages, downloadDanmaku, aid)
}

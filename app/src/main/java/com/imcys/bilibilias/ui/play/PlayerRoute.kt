package com.imcys.bilibilias.ui.play

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.imcys.bilibilias.base.utils.sharedHiltViewModel

const val ROUTE_PLAYER = "player"
fun NavController.navigateToPlayer() {
    navigate(ROUTE_PLAYER)
}

fun NavGraphBuilder.playerRoute(
    onNavigateToDownloadOption: () -> Unit,
    onNavigateToDownloadAanmaku: () -> Unit,
    navController: NavHostController
) = composable(ROUTE_PLAYER) { backStackEntry ->
    val viewModel: PlayerViewModel = backStackEntry.sharedHiltViewModel(navController)
    val state by viewModel.playerState.collectAsStateWithLifecycle()
    PlayerRoute(
        onNavigateToDownloadOption,
        state,
        onNavigateToDownloadAanmaku,
        viewModel::selectedQuality,
        viewModel::selectedPage
    )
}

@Composable
fun PlayerRoute(
    onNavigateToDownloadOption: () -> Unit,
    state: PlayerState,
    onNavigateToDownloadAanmaku: () -> Unit,
    selectedQuality: (Int) -> Unit,
    selectedPage: (Long, Long) -> Unit,
) {
    PlayerScreen(
        state,
        onNavigateToDownloadOption,
        onNavigateToDownloadAanmaku,
        selectedQuality,
        selectedPage
    )
}

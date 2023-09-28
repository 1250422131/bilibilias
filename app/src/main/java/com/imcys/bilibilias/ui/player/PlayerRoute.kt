package com.imcys.bilibilias.ui.player

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavBackStackEntry
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
    onBack: () -> Unit,
    onNavigateToDownloadOption: () -> Unit,
    navController: NavHostController
) = composable(ROUTE_PLAYER) { backStackEntry ->
    PlayerRoute(
        backStackEntry,
        navController,
        onBack,
        onNavigateToDownloadOption
    )
}

@Composable
fun PlayerRoute(
    backStackEntry: NavBackStackEntry,
    navController: NavHostController,
    onBack: () -> Unit,
    onNavigateToDownloadOption: () -> Unit
) {
    val viewModel: PlayerViewModel = backStackEntry.sharedHiltViewModel(navController = navController)
    val state by viewModel.event.collectAsStateWithLifecycle()
    PlayerScreen(state, onNavigateToDownloadOption, viewModel::changeUrl,viewModel::getVideoPlayList)
}

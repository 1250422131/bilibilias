package com.imcys.bilibilias.ui.download

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.google.accompanist.navigation.material.ExperimentalMaterialNavigationApi
import com.google.accompanist.navigation.material.bottomSheet
import com.imcys.bilibilias.base.utils.sharedHiltViewModel
import com.imcys.bilibilias.ui.player.PlayerViewModel

const val ROUTE_DOWNLOAD = "download"
fun NavController.navigateToDownload() {
    navigate(ROUTE_DOWNLOAD) {
        launchSingleTop = true
    }
}

fun NavGraphBuilder.downloadRoute(
    onNavigateTo: () -> Unit,
    onBack: () -> Unit
) = composable(ROUTE_DOWNLOAD) {
    val viewModel: DownloadViewModel = hiltViewModel()
    val state by viewModel.state.collectAsStateWithLifecycle()
    val groupDownloadProgress = remember { viewModel.groupDownloadProgress }
    LaunchedEffect(Unit) {
        viewModel.allTask()
    }
    DownloadRoute(state,groupDownloadProgress)
}

@Composable
fun DownloadRoute(state: DownloadState, groupDownloadProgress: GroupDownloadProgress) {
    DownloadListScreen(state,groupDownloadProgress)
}

const val ROUTE_DOWNLOAD_OPTIONS = "download_options"
fun NavController.navigateToDownloadOptions() {
    navigate(ROUTE_DOWNLOAD_OPTIONS)
}

@OptIn(ExperimentalMaterialNavigationApi::class)
fun NavGraphBuilder.downloadOptionsRoute(
    onBack: () -> Unit,
    navController: NavHostController,
) = bottomSheet(
    ROUTE_DOWNLOAD_OPTIONS,
) { backStackEntry ->
    val playerViewModel: PlayerViewModel = backStackEntry.sharedHiltViewModel(navController)
    val downloadViewModel: DownloadViewModel = hiltViewModel()
    val state by playerViewModel.event.collectAsStateWithLifecycle()

    DownloadOptionsScreen(
        state = state,
        onBack = onBack,
        downloadOptions = playerViewModel.downloadOptions,
        downloadViewModel::downloadVideo
    )
}

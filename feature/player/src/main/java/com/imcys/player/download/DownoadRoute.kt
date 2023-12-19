package com.imcys.player.download

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.google.accompanist.navigation.material.ExperimentalMaterialNavigationApi
import com.imcys.common.utils.sharedHiltViewModel
import com.imcys.model.video.PageData
import com.imcys.network.download.DownloadListHolders
import com.imcys.network.download.DownloadToolType
import com.imcys.player.PlayerState
import com.imcys.player.PlayerViewModel

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
    DownloadRoute(state, viewModel::deleteFileById)
}

@Composable
fun DownloadRoute(
    state: DownloadListState,
    deleteFile: (Int) -> Unit
) {
    DownloadListScreen(state, deleteFile, state.bvGroup)
}

const val ROUTE_DOWNLOAD_OPTIONS = "download_options"
fun NavController.navigateToDownloadOptions() {
    navigate(ROUTE_DOWNLOAD_OPTIONS)
}

@OptIn(ExperimentalMaterialNavigationApi::class)
fun NavGraphBuilder.downloadOptionsRoute(
    onBack: () -> Unit,
    navController: NavHostController,
) = composable(
    ROUTE_DOWNLOAD_OPTIONS,
) { backStackEntry ->
    val playerViewModel: PlayerViewModel = backStackEntry.sharedHiltViewModel(navController)
    val downloadViewModel: DownloadViewModel = backStackEntry.sharedHiltViewModel(navController)
    val state by playerViewModel.playerState.collectAsStateWithLifecycle()
    val downloadListState by downloadViewModel.state.collectAsStateWithLifecycle()
    DownloadOptionsRoute(
        state = state,
        onBack = onBack,
        downloadListHolders = playerViewModel.downloadListHolders,
        downloadVideo = downloadViewModel::downloadVideo,
        setDownloadTool = downloadViewModel::setDownloadTool,
        setRequireDownloadFileType = downloadViewModel::setRequireDownloadFileType,
        downloadListState = downloadListState,
        setAcceptDescription = downloadViewModel::setAcceptDescription,
        setPages = downloadViewModel::setPages
    )
}

@Composable
fun DownloadOptionsRoute(
    state: PlayerState,
    onBack: () -> Unit,
    downloadListHolders: DownloadListHolders,
    downloadVideo: (com.imcys.model.VideoDetails, DownloadListHolders) -> Unit,
    setDownloadTool: (DownloadToolType) -> Unit,
    setRequireDownloadFileType: (DownloadFileType) -> Unit,
    downloadListState: DownloadListState,
    setAcceptDescription: (Int) -> Unit,
    setPages: (PageData) -> Unit,
) {
    DownloadOptionsScreen(
        state = state,
        onBack = onBack,
        downloadOptions = downloadListHolders,
        downloadVideo = downloadVideo,
        setDownloadTool = setDownloadTool,
        downloadListState = downloadListState,
        setRequireDownloadFileType = setRequireDownloadFileType,
        setAcceptDescription = setAcceptDescription,
        setPages = setPages,
        )
}

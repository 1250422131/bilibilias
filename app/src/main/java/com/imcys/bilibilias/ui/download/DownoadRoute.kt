package com.imcys.bilibilias.ui.download

import androidx.compose.foundation.clickable
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
    DownloadRoute()
}

@Composable
fun DownloadRoute() {
    Download()
}

const val ROUTE_DOWNLOAD_OPTIONS = "download_options"
const val ROUTE_DOWNLOAD_VIDEO_CLARITY = "video_clarity"
const val ROUTE_DOWNLOAD_CACHED_SUBSET = "cached_subset"
const val ROUTE_DOWNLOAD_CACHE_TYPE = "cache_type"
const val ROUTE_DOWNLOAD_CACHED_AUDIO_QUALITY = "cached_audio_quality"
fun NavController.navigateToDownloadOptions() {
    navigate(ROUTE_DOWNLOAD_OPTIONS)
}

fun NavController.navigateToVideoClarity() {
    navigate(ROUTE_DOWNLOAD_VIDEO_CLARITY)
}

fun NavController.navigateToCachedSubset() {
    navigate(ROUTE_DOWNLOAD_CACHED_SUBSET)
}

fun NavController.navigateToCacheType() {
    navigate(ROUTE_DOWNLOAD_CACHE_TYPE)
}

fun NavController.navigateToCachedAudioQuality() {
    navigate(ROUTE_DOWNLOAD_CACHED_AUDIO_QUALITY)
}

@OptIn(ExperimentalMaterialNavigationApi::class)
fun NavGraphBuilder.downloadOptionsRoute(
    onNavigateToVideoClarity: () -> Unit,
    onNavigateToCachedSubset: () -> Unit,
    onNavigateToCacheType: () -> Unit,
    onNavigateToCachedAudioQuality: () -> Unit,
    onBack: () -> Unit,
    navController: NavHostController,
) = bottomSheet(
    ROUTE_DOWNLOAD_OPTIONS,
) { backStackEntry ->
    val viewModel: PlayerViewModel = backStackEntry.sharedHiltViewModel(navController)
    val state by viewModel.event.collectAsStateWithLifecycle()

    DownloadOptionsScreen(
        state = state,
        onNavigateToVideoClarity = onNavigateToVideoClarity,
        onNavigateToCachedSubset = onNavigateToCachedSubset,
        onNavigateToCacheType = onNavigateToCacheType,
        onNavigateToCachedAudioQuality = onNavigateToCachedAudioQuality,
        onBack = onBack,
        downloadOptions = viewModel.downloadOptions,
        getVideoPlayList = viewModel::getVideoPlayList,
        downloadVideo = viewModel::downloadVideo,
    )
}

@OptIn(ExperimentalMaterialNavigationApi::class)
fun NavGraphBuilder.downloadVideoClarityRoute(
    onBack: () -> Unit,
    navController: NavHostController
) = bottomSheet(
    ROUTE_DOWNLOAD_VIDEO_CLARITY,
) { navBackStackEntry ->
    val viewModel: PlayerViewModel = navBackStackEntry.sharedHiltViewModel(navController = navController)
    val state by viewModel.event.collectAsStateWithLifecycle()
    var desc by remember { mutableStateOf(state.videoPlayDetails.acceptDescription[0]) }
    LazyColumn {
        items(state.videoPlayDetails.acceptDescription) { item ->
            Text(
                item,
                modifier = Modifier.clickable { desc = item; onBack() },
                color = if (desc == item) MaterialTheme.colorScheme.primary else Color.Unspecified
            )
        }
    }
}

@OptIn(ExperimentalMaterialNavigationApi::class)
fun NavGraphBuilder.downloadCachedSubsetRoute(
    onBack: () -> Unit
) = bottomSheet(ROUTE_DOWNLOAD_CACHED_SUBSET) {
    Text(ROUTE_DOWNLOAD_CACHED_SUBSET)
}

@OptIn(ExperimentalMaterialNavigationApi::class)
fun NavGraphBuilder.downloadCacheTypeRoute(
    onBack: () -> Unit
) = bottomSheet(ROUTE_DOWNLOAD_CACHE_TYPE) {
    Text(ROUTE_DOWNLOAD_CACHE_TYPE)
}

@OptIn(ExperimentalMaterialNavigationApi::class)
fun NavGraphBuilder.downloadCachedAudioQualityRoute(
    onBack: () -> Unit
) = bottomSheet(ROUTE_DOWNLOAD_CACHED_AUDIO_QUALITY) {
    Text(ROUTE_DOWNLOAD_CACHED_AUDIO_QUALITY)
}

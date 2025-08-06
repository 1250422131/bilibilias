package com.imcys.bilibilias.ui.download.navigation

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.compose.composable
import androidx.navigation3.runtime.NavKey
import com.imcys.bilibilias.database.entity.download.DownloadSegment
import com.imcys.bilibilias.ui.download.DownloadScreen
import kotlinx.serialization.Serializable

const val DOWNLOAD_PATH = "download"


@Serializable
object DownloadRoute: NavKey


fun NavController.navigateToDownload(
    homeRoute: DownloadRoute = DownloadRoute,
    builder: (NavOptionsBuilder.() -> Unit)? = null
) = navigate(homeRoute, builder ?: {})


@OptIn(ExperimentalSharedTransitionApi::class)
fun NavGraphBuilder.downloadScreen(
    onToBack: () -> Unit,
    onPlay: (DownloadSegment) -> Unit,
) {
    composable<DownloadRoute> { navBackStackEntry ->
        DownloadScreen(onToBack, onPlay)
    }
}
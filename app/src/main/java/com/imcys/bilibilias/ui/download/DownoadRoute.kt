package com.imcys.bilibilias.ui.download

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable

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

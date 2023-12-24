package com.bilias.feature.download.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.bilias.feature.download.DownloadRoute

const val ROUTE_DOWNLOAD = "download"

fun NavController.navigateToDownload() {
    navigate(ROUTE_DOWNLOAD) {
        launchSingleTop = true
    }
}

fun NavGraphBuilder.downloadRoute() = composable(ROUTE_DOWNLOAD) {
   DownloadRoute()
}

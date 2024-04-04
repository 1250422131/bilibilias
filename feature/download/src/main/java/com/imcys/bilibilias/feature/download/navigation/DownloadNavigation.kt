package com.imcys.bilibilias.feature.download.navigation

import androidx.compose.runtime.Composable
import com.imcys.bilibilias.feature.download.DownloadRoute
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootNavGraph

@RootNavGraph(start = true)
@Destination
@Composable
fun NavigationToDownload() {
    DownloadRoute()
}

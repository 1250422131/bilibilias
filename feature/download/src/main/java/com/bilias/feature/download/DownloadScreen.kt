package com.bilias.feature.download

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ListItem
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
internal fun DownloadRoute() {
    DownloadScreen()
}

@Composable
internal fun DownloadScreen() {
    LazyColumn(modifier = Modifier.fillMaxSize()) {
        item {
            ListItem(headlineContent = { /*TODO*/ })
        }
    }
}

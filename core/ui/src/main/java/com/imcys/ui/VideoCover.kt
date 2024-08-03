package com.imcys.ui

import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage

@Composable
fun VideoCover(url: String) {
    AsyncImage(
        model = url,
        contentDescription = null,
        modifier = Modifier.size(128.dp, 72.dp),
        contentScale = ContentScale.Crop,
    )
}

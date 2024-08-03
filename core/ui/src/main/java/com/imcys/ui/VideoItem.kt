package com.imcys.ui

import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun VideoItem(url: String, title: String, modifier: Modifier = Modifier) {
    ListItem(
        modifier = modifier,
        leadingContent = {
            VideoCover(url = url)
        },
        headlineContent = {
            Text(text = title)
        }
    )
}

package com.bilias.feature.download

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.imcys.ui.VideoItem

@Composable
internal fun DownloadRoute() {
    val viewModel: DownloadViewModel = hiltViewModel()
    val taskState by viewModel.taskState.collectAsStateWithLifecycle()
    DownloadScreen(taskState)
}

@Composable
internal fun DownloadScreen(taskState: TaskState) {
    LazyColumn(modifier = Modifier.fillMaxSize()) {
        items(taskState.taskList) { task ->
            VideoItem(url = task.cover, title = task.title)
        }
    }
}

package com.imcys.bilibilias.ui.download

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier

@Composable
fun DownloadListScreen(state: DownloadState, groupDownloadProgress: GroupDownloadProgress) {
    val progress = remember { groupDownloadProgress }
    val groupTask = remember(progress.groupTask) { progress.groupTask }
    LazyColumn(Modifier.fillMaxSize()) {
        items(state.tasks) {
            Text(
                when (it.state) {
                    0 -> "NONE"
                    1 -> "QUEUED"
                    2 -> "DOWNLOADING"
                    3 -> "PAUSED"
                    4 -> "COMPLETED"
                    5 -> "CANCELLED"
                    6 -> "FAILED"
                    7 -> "REMOVED"
                    8 -> "DELETED"
                    9 -> "ADDED"
                    else -> "NONE"
                }
            )
        }
        items(groupTask.entries.toList()) { (k, v) ->
            Text("key=$k,value=$v")
        }
    }
}

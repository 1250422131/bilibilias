package com.imcys.player.download

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.media3.exoplayer.offline.Download

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun DownloadListScreen(
    state: DownloadListState,
    deleteFile: (Int) -> Unit,
    taskGroup: Map<String, List<Download>>
) {
    LazyColumn(Modifier.fillMaxSize(), contentPadding = PaddingValues(4.dp)) {
        taskGroup.forEach { (bvid, downloads) ->
            stickyHeader(key = bvid) {
                Text(bvid)
            }

            // items(downloads, key = { it.id }) { item ->
            //     val dismissState = rememberDismissState(confirmValueChange = { dismissValue ->
            //         if (dismissValue == DismissValue.DismissedToStart) {
            //             deleteFile(item.id)
            //         }
            //         true
            //     })
            //     SwipeToDismiss(
            //         state = dismissState,
            //         modifier = Modifier.animateItemPlacement(),
            //         directions = setOf(DismissDirection.EndToStart),
            //         background = {
            //             val color by animateColorAsState(
            //                 when (dismissState.targetValue) {
            //                     DismissValue.DismissedToStart -> MaterialTheme.colorScheme.inversePrimary
            //                     else -> Color.Transparent
            //                 },
            //                 label = "颜色"
            //             )
            //             val scale by animateFloatAsState(
            //                 if (dismissState.targetValue == DismissValue.Default) 0.75f else 1f,
            //                 label = "缩放"
            //             )
            //             Box(
            //                 Modifier
            //                     .fillMaxSize()
            //                     .background(color)
            //                     .padding(horizontal = 20.dp),
            //                 contentAlignment = Alignment.CenterEnd
            //             ) {
            //                 Icon(
            //                     Icons.Default.Delete,
            //                     contentDescription = "Localized description",
            //                     modifier = Modifier.scale(scale)
            //                 )
            //             }
            //         },
            //         dismissContent = {
            //             // ListItem(headlineContent = {  })
            //             Card(Modifier.padding(4.dp)) {
            //                 ListItem(headlineContent = {
            //                     // Text(item.extras.getString(EXTRAS_TITLE, ""))
            //                     Text(item.status.name + item.title, Modifier, maxLines = 2)
            //                 }, leadingContent = {
            //                     // Text(item.tag ?: TAG_VIDEO)
            //                 })
            //             }
            //         }
            //     )
            // }
        }
    }
}

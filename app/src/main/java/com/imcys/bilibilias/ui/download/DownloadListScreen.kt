package com.imcys.bilibilias.ui.download

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.DismissDirection
import androidx.compose.material3.DismissValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SwipeToDismiss
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDismissState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.tonyodev.fetch2.Download

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun DownloadListScreen(
    state: DownloadListState,
    deleteFile: (Int) -> Unit,
    taskGroup: Map<String, List<Download>>
) {
    val list = remember { mutableStateListOf<Int>() }

    LaunchedEffect(Unit) {
        repeat(100) {
            list.add(it)
        }
    }
    LazyColumn(Modifier.fillMaxSize(), contentPadding = PaddingValues(4.dp)) {
        taskGroup.forEach { (bvid, downloads) ->
            stickyHeader(key = bvid) {
                Text(bvid)
            }

            items(downloads, key = { it.id }) { item ->
                val dismissState = rememberDismissState(confirmValueChange = { dismissValue ->
                    if (dismissValue == DismissValue.DismissedToStart) {
                        deleteFile(item.id)
                    }
                    true
                })
                SwipeToDismiss(
                    state = dismissState,
                    modifier = Modifier.animateItemPlacement(),
                    directions = setOf(DismissDirection.EndToStart),
                    background = {
                        val color by animateColorAsState(
                            when (dismissState.targetValue) {
                                DismissValue.DismissedToStart -> MaterialTheme.colorScheme.inversePrimary
                                else -> Color.Transparent
                            },
                            label = "颜色"
                        )
                        val scale by animateFloatAsState(
                            if (dismissState.targetValue == DismissValue.Default) 0.75f else 1f,
                            label = "缩放"
                        )
                        Box(
                            Modifier
                                .fillMaxSize()
                                .background(color)
                                .padding(horizontal = 20.dp),
                            contentAlignment = Alignment.CenterEnd
                        ) {
                            Icon(
                                Icons.Default.Delete,
                                contentDescription = "Localized description",
                                modifier = Modifier.scale(scale)
                            )
                        }
                    },
                    dismissContent = {
                        // ListItem(headlineContent = {  })
                        Card(Modifier.padding(4.dp)) {
                            ListItem(headlineContent = {
                                // Text(item.extras.getString(EXTRAS_TITLE, ""))
                                Text(item.status.name + item.title, Modifier, maxLines = 2)
                            }, leadingContent = {
                                // Text(item.tag ?: TAG_VIDEO)
                            })
                        }
                    }
                )
            }
        }
    }
}

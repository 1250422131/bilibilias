package com.imcys.player.sheet

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.imcys.model.video.Page
import kotlinx.collections.immutable.ImmutableList

@Composable
internal fun SheetDownloadVideo(
    qualityDescriptionList: ImmutableList<Pair<String, Int>>,
    onVideoQualityChanged: (Int) -> Unit,
    downloadQuality: Int?,
    addToDownloadQueue: (Page) -> Unit,
    addAllToDownloadQueue: (List<Page>) -> Unit,
    pageList: List<Page>,
) {
    var openSetting by remember { mutableStateOf(false) }
    Column {
        Row {
            Spacer(modifier = Modifier.weight(1f))
            IconButton(onClick = { openSetting = true }, modifier = Modifier) {
                Icon(imageVector = Icons.Default.Settings, contentDescription = "设置")
            }
        }
        LazyRow(Modifier.fillMaxWidth()) {
            items(qualityDescriptionList) { pair ->
                TextButton(
                    onClick = { onVideoQualityChanged(pair.second) },
                    border = if (downloadQuality == pair.second) {
                        BorderStroke(1.dp, MaterialTheme.colorScheme.primary)
                    } else {
                        null
                    }
                ) {
                    Text(text = pair.first, fontSize = 11.sp)
                }
            }
        }
        Box {
            LazyColumn(contentPadding = PaddingValues(8.dp, 4.dp)) {
                items(pageList) { item ->
                    var selected by remember { mutableStateOf(false) }
                    TextButton(
                        onClick = { addToDownloadQueue(item);selected = true },
                        border = if (selected) {
                            BorderStroke(1.dp, MaterialTheme.colorScheme.primary)
                        } else {
                            null
                        }
                    ) {
                        Text(
                            text = item.toString(),
                            modifier = Modifier
                                .fillMaxWidth(),
                            maxLines = 1
                        )
                    }
                }
            }
            Button(
                onClick = { addAllToDownloadQueue(pageList) },
                Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
            ) {
                Text(text = "全部下载")
            }
        }
    }
}

package com.imcys.player.sheet

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.imcys.player.state.PlayInfoUiState
import kotlinx.collections.immutable.ImmutableList

@Composable
internal fun SheetPage(
    qualityDescriptionList: ImmutableList<Pair<String, Int>>,
    addToDownloadQueue: (List<String>, Int) -> Unit,
    archives: PlayInfoUiState,
) {
    Column {
        // 清晰度选择
        var quality by remember { mutableStateOf(qualityDescriptionList.first()) }
        LazyRow {
            items(qualityDescriptionList) { pair ->
                TextButton(
                    onClick = { quality = pair },
                    border = if (quality == pair) {
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
            // 选集
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(8.dp, 4.dp)
            ) {
                if (archives is PlayInfoUiState.Success) {
                    items(archives.archives) { item ->
                        var selected by remember { mutableStateOf(false) }
                        TextButton(
                            onClick = {
                                // addToDownloadQueue(listOf(item), quality.second); selected = true
                            },
                            border = if (selected) {
                                BorderStroke(1.dp, MaterialTheme.colorScheme.primary)
                            } else {
                                null
                            },

                            modifier = Modifier.combinedClickable(onLongClick = {}) { }
                        ) {
                            Text(
                                text = item.title,
                                modifier = Modifier
                                    .fillMaxWidth(),
                                maxLines = 1
                            )
                        }
                    }
                }
            }
        }
        Button(
            onClick = { addToDownloadQueue(emptyList(), quality.second) },
            Modifier
                .fillMaxWidth()
        ) {
            Text(text = "全部下载")
        }
    }
}

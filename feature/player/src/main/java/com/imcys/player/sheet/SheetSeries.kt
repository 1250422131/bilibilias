package com.imcys.player.sheet

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.imcys.designsystem.component.AsButton
import com.imcys.designsystem.component.FormatTag
import com.imcys.player.state.PlayInfoUiState
import kotlinx.collections.immutable.ImmutableList

@Composable
internal fun SheetSeries(
    qualityDescriptionList: ImmutableList<Pair<String, Int>>,
    addToDownloadQueue: (List<String>, Int) -> Unit,
    archives: PlayInfoUiState,
) {
    Column {
        var quality by remember { mutableStateOf(qualityDescriptionList.first()) }
        LazyRow {
            items(qualityDescriptionList) { pair ->
                FormatTag(
                    selected = quality == pair,
                    onClick = { quality = pair }
                ) {
                    Text(text = pair.first, fontSize = 11.sp)
                }
            }
        }
        Box {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(bottom = 60.dp)
            ) {
                if (archives is PlayInfoUiState.Success) {
                    items(archives.archives) { item ->
                        AsButton(
                            onClick = {
                                addToDownloadQueue(listOf(item.bvid), quality.second)
                            },
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
//            AsButton(
//                onClick = { addToDownloadQueue(listOf(item), quality.second) },
//                modifier = Modifier
//                    .fillMaxWidth()
//            ) {
//                Text(text = "全部下载")
//            }
        }
    }
}

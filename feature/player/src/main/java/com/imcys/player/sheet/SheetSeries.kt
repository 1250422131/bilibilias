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
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.imcys.designsystem.component.AsButton
import com.imcys.designsystem.component.FormatTag
import com.imcys.player.state.PlayInfoUiState
import com.imcys.player.state.Quality
import kotlinx.collections.immutable.ImmutableMap

@Composable
internal fun SheetSeries(
    descriptionWithQuality: ImmutableMap<Quality, String>,
    addToDownloadQueue: (List<String>, Int) -> Unit,
    playInfoUiState: PlayInfoUiState,
    defaultQuality: Int,
) {
    Column {
        var selectedQuality by remember(defaultQuality) { mutableIntStateOf(defaultQuality) }
        LazyRow {
            for ((quality, description) in descriptionWithQuality) {
                item {
                    FormatTag(
                        selected = selectedQuality == quality,
                        onClick = { selectedQuality = quality }
                    ) {
                        Text(text = description, fontSize = 11.sp)
                    }
                }
            }
        }
        Box {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(bottom = 60.dp)
            ) {
                if (playInfoUiState is PlayInfoUiState.Success) {
                    items(playInfoUiState.series) { item ->
                        AsButton(
                            onClick = {
                                addToDownloadQueue(listOf(item.bvid), selectedQuality)
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
        }
    }
}


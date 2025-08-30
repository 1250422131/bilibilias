package com.imcys.bilibilias.ui.search

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.DownloadDone
import androidx.compose.material.icons.rounded.FileDownload
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.imcys.bilibilias.core.domain.model.EpisodeCacheState
import com.imcys.bilibilias.core.domain.model.EpisodeCacheStatus
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun EpisodeList(
    episodes: List<EpisodeCacheState>,
    modifier: Modifier = Modifier,
    onSelectedEpisode: (EpisodeCacheState) -> Unit
) {
    val listState = rememberLazyListState()
    LazyColumn(state = listState, modifier = modifier) {
        items(episodes, key = { it.episodeSubId }) { item ->
            EpisodeItem(item) {
                onSelectedEpisode(item)
            }
        }
    }
}

@Composable
private fun EpisodeItem(cacheState: EpisodeCacheState, onSelectedEpisodeSubId: (Long) -> Unit) {
    val canCache = cacheState.cacheStatus == EpisodeCacheStatus.NotCached
    OutlinedCard(modifier = Modifier.padding(vertical = 4.dp)) {
        Row(
            modifier = Modifier.clickable(enabled = canCache) {
                if (canCache) {
                    onSelectedEpisodeSubId(cacheState.episodeSubId)
                }
            },
            verticalAlignment = Alignment.CenterVertically
        ) {
            VerticallyCenteredSingleLineText(
                cacheState.title,
                modifier = Modifier
                    .height(50.dp)
                    .weight(5f)
                    .padding(start = 8.dp),
            )
            if (canCache) {
                Icon(Icons.Rounded.FileDownload, null, Modifier.weight(1f))
            } else {
                Icon(Icons.Rounded.DownloadDone, null, Modifier.weight(1f))
            }
        }
    }
}


@Composable
fun VerticallyCenteredSingleLineText(
    text: String,
    textAlign: TextAlign = TextAlign.Start,
    modifier: Modifier = Modifier
) {
    Text(
        text = text,
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight(align = Alignment.CenterVertically),
        textAlign = textAlign,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis
    )
}

@Preview(showBackground = true)
@Composable
fun VerticallyCenteredSingleLineTextPreview() {
    VerticallyCenteredSingleLineText(
        text = "This is a sample text for preview.",
        textAlign = TextAlign.Center
    )
}
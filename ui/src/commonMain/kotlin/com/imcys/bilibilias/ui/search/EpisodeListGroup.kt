package com.imcys.bilibilias.ui.search

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
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
import androidx.compose.material.icons.rounded.KeyboardArrowDown
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.imcys.bilibilias.core.domain.model.EpisodeCacheRequest
import com.imcys.bilibilias.core.domain.model.EpisodeCacheState
import com.imcys.bilibilias.core.domain.model.EpisodeCacheStatus
import com.imcys.bilibilias.core.domain.model.MediaStream
import com.imcys.bilibilias.ui.component.AnimatedBottomSheet
import org.jetbrains.compose.ui.tooling.preview.Preview

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EpisodeListGroup(
    visible: Boolean,
    episodes: List<EpisodeCacheState>,
    mediaStreams: List<MediaStream>,
    onDismiss: () -> Unit,
    onRequestCache: (episode: EpisodeCacheState, request: EpisodeCacheRequest) -> Unit = { _, _ -> },
) {
    var showMediaResolutionSelector by remember { mutableStateOf(false) }
    var currentResolution by remember { mutableStateOf(mediaStreams.first()) }
    val requestCache: (episode: EpisodeCacheState) -> Unit = { episodeCacheState ->
        onRequestCache(
            episodeCacheState,
            EpisodeCacheRequest(
                episodeId = episodeCacheState.episodeId,
                episodeSubId = episodeCacheState.episodeSubId,
                videoResolution = Int.MAX_VALUE,
                audioResolution = Int.MAX_VALUE
            )
        )
    }
    AnimatedBottomSheet(
        isVisible = visible,
        onDismissRequest = onDismiss,
    ) {
        Column(
            modifier = Modifier
                .padding(20.dp, 0.dp, 20.dp, 20.dp)
        ) {
            Row(
                modifier = Modifier
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null
                    ) { showMediaResolutionSelector = true },
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(currentResolution.description)
                Icon(Icons.Rounded.KeyboardArrowDown, null)
            }
            EpisodeList(episodes) {
                requestCache(it)
            }
        }
    }

    MediaResolutionSelector(
        isVisible = showMediaResolutionSelector,
        mediaStreams = mediaStreams,
        onDismiss = { showMediaResolutionSelector = false },
        onResolutionSelected = {
            currentResolution = it
        },
    )
}

@Composable
private fun EpisodeList(
    episodes: List<EpisodeCacheState>,
    onSelectedEpisode: (EpisodeCacheState) -> Unit
) {
    val listState = rememberLazyListState()
    Box(modifier = Modifier.fillMaxHeight(0.7f)) {
        LazyColumn(state = listState) {
            items(episodes, key = { it.episodeSubId }) { item ->
                EpisodeItem(item) {
                    onSelectedEpisode(item)
                }
            }
        }
        Row(modifier = Modifier.padding(16.dp).align(Alignment.BottomCenter)) {
            Text("bottom")
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


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AsModalBottomSheet(
    visible: Boolean,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
    sheetState: SheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
    containerColor: Color = MaterialTheme.colorScheme.surfaceContainer,
    scrimColor: Color = BottomSheetDefaults.ScrimColor,
    content: @Composable ColumnScope.() -> Unit
) {
    AnimatedVisibility(visible = visible) {
        ModalBottomSheet(
            onDismissRequest = onDismiss,
            sheetState = sheetState,
            modifier = modifier,
            containerColor = containerColor,
            scrimColor = scrimColor,
            content = content
        )
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

@Preview
@Composable
fun PreviewVideoDownloadDialog() {
//    VideoDownloadDialog()
}
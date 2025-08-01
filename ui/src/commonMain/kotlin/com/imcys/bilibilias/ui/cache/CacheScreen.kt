package com.imcys.bilibilias.ui.cache

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Merge
import androidx.compose.material.icons.outlined.ArrowOutward
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxState
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.imcys.bilibilias.core.domain.model.CacheEpisodeState
import com.imcys.bilibilias.core.model.DataUnit
import com.imcys.bilibilias.logic.cache.CacheComponent
import com.imcys.bilibilias.ui.runtime.collectAsStateWithLifecycle

@Composable
fun CacheScreen(component: CacheComponent) {
    val state by component.stateFlow.collectAsStateWithLifecycle()

    val canMux by component.canProcess.collectAsStateWithLifecycle()

    CaCheContent(
        state,
        onDelete = component::deleteEpisodeCache,
        canMux = canMux,
        onCombine = component::onCombine
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CaCheContent(
    cacheEpisodeState: List<CacheEpisodeState>,
    canMux: Boolean,
    onDelete: (CacheEpisodeState) -> Unit = { },
    onCombine: (CacheEpisodeState) -> Unit = { },
) {
    Scaffold { innerPadding ->
        LazyColumn(modifier = Modifier.padding(innerPadding)) {
            items(cacheEpisodeState, key = { it.episodeMetadata.cid }) { item ->
                val swipeToDismissBoxState = rememberSwipeToDismissBoxState(
                    confirmValueChange = {
                        when (it) {
                            SwipeToDismissBoxValue.StartToEnd -> {
                                onCombine(item)
                                false
                            }

                            SwipeToDismissBoxValue.EndToStart -> {
                                onDelete(item)
                                true
                            }

                            SwipeToDismissBoxValue.Settled -> true
                        }
                    },
                    positionalThreshold = { totalDistance -> totalDistance * 0.3f }
                )
                SwipeToDismissBox(
                    state = swipeToDismissBoxState,
                    backgroundContent = {
                        swipeToDismissBoxState.SwipeDismissBackground()
                    },
                    enableDismissFromStartToEnd = canMux,
                    modifier = Modifier.animateItem(),
                ) {
                    CacheEpisodeItem(item)
                    if (cacheEpisodeState.last() != item) {
                        HorizontalDivider(modifier = Modifier.padding(horizontal = 8.dp))
                    }
                }
            }
        }
    }
}

@Composable
private fun SwipeToDismissBoxState.SwipeDismissBackground() {
    // Cross-fade the background color as the drag gesture progresses.
    val color by animateColorAsState(
        when (targetValue) {
            SwipeToDismissBoxValue.Settled -> Color.LightGray
            SwipeToDismissBoxValue.StartToEnd ->
                lerp(
                    Color.LightGray,
                    Color.Blue,
                    progress
                )

            SwipeToDismissBoxValue.EndToStart ->
                lerp(
                    Color.LightGray,
                    Color.Red,
                    progress
                )
        },
        label = "swipeable card item background color"
    )
    Row(
        modifier = Modifier
            .padding(8.dp)
            .clip(RoundedCornerShape(12.0.dp))
            .background(color)
            .fillMaxSize(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        when (dismissDirection) {
            SwipeToDismissBoxValue.EndToStart -> {
                Spacer(modifier = Modifier)
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Remove item",
                    tint = Color.White,
                    modifier = Modifier
                        .padding(12.dp)
                )
            }

            SwipeToDismissBoxValue.StartToEnd -> {
                Icon(
                    Icons.Default.Merge,
                    contentDescription = null,
                    modifier = Modifier
                        .padding(12.dp),
                    tint = Color.White
                )
            }

            SwipeToDismissBoxValue.Settled -> {
            }
        }
    }
}

@Composable
private fun CacheEpisodeItem(state: CacheEpisodeState) {
    Card(modifier = Modifier.padding(8.dp)) {
        Row(
            modifier = Modifier.padding(8.dp).fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    state.episodeMetadata.title,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.titleLarge
                )
                Text(state.fileStats.downloadedBytes.toString(DataUnit.MEGABYTES))
            }
            Row {
                IconButton({}) {
                    Icon(Icons.Outlined.Info, contentDescription = null)
                }
                IconButton({}) {
                    Icon(Icons.Outlined.ArrowOutward, contentDescription = null)
                }
            }
        }
    }
}
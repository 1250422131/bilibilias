package com.imcys.bilibilias.feature.tool

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsBottomHeight
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyGridScope
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.imcys.bilibilias.core.ui.TrackScreenViewEvent
import com.imcys.bilibilias.core.ui.TrackScrollJank

@Preview(showBackground = true)
@Composable
fun FormatScreen(modifier: Modifier = Modifier) {
    FormatContent()
}

@Composable
fun FormatContent(modifier: Modifier = Modifier) {
    // This code should be called when the UI is ready for use and relates to Time To Full Display.
    // ReportDrawnWhen { !isSyncing && !isOnboardingLoading && !isFeedLoading }

    val state = rememberLazyGridState()
    // val scrollbarState = state.scrollbarState(
    //     itemsAvailable = itemsAvailable,
    // )
    TrackScrollJank(scrollableState = state, stateName = "tool:format")

    Box(
        modifier = modifier
            .fillMaxSize(),
    ) {
        // verticalItemSpacing = 24.dp,
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            contentPadding = PaddingValues(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier
                .testTag("tool:format"),
            state = state,
        ) {
            formating()
            item(contentType = "bottomSpacing") {
                Column {
                    Spacer(modifier = Modifier.height(8.dp))
                    // Add space for the content to clear the "offline" snackbar.
                    // TODO: Check that the Scaffold handles this correctly in NiaApp
                    // if (isOffline) Spacer(modifier = Modifier.height(48.dp))
                    Spacer(Modifier.windowInsetsBottomHeight(WindowInsets.safeDrawing))
                }
            }
        }
    }
    TrackScreenViewEvent(screenName = "Format")
}

private fun LazyGridScope.formating(
    interestsItemModifier: Modifier = Modifier,
) {
    item(span = { GridItemSpan(2) }) {
        Text("视频（无音轨）", modifier = Modifier.padding(8.dp))
    }
    items(5) {
        OutlinedCard(onClick = {}) {
            Column(modifier = Modifier.padding(8.dp)) {
                Text("360 P（HEV1）")
                Spacer(Modifier.height(20.dp))
                Row {
                    Text("30.44 MB", modifier = Modifier.weight(1f))
                    Text("102.9 Kbps", modifier = Modifier.weight(1f))
                }
            }
        }
    }
    item {
        Text("音频")
    }
    items(5) {
        Text("StaggeredGridItemSpan.FullLine")
    }
}
private fun LazyGridScope.sourcesCard(
    title: String,
) {
    item(span = { GridItemSpan(2) }) {
        Text(title, modifier = Modifier.padding(8.dp))
    }
    items(5) {
        OutlinedCard(onClick = {}) {
            Column(modifier = Modifier.padding(8.dp)) {
                Text("360 P（HEV1）")
                Spacer(Modifier.height(20.dp))
                Row {
                    Text("30.44 MB", modifier = Modifier.weight(1f))
                    Text("102.9 Kbps", modifier = Modifier.weight(1f))
                }
            }
        }
    }
}

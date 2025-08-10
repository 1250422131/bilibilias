package com.imcys.bilibilias.ui.search

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedCard
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.imcys.bilibilias.core.domain.model.MediaStream
import com.imcys.bilibilias.ui.component.AnimatedBottomSheet

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MediaResolutionSelector(
    isVisible: Boolean,
    mediaStreams: List<MediaStream>,
    onDismiss: () -> Unit,
    onResolutionSelected: (MediaStream) -> Unit = {},
) {
    val listState = rememberLazyListState()
    val uniqueMediaStream = remember(mediaStreams) { mediaStreams.distinctBy { it.id } }

    AnimatedBottomSheet(
        isVisible,
        onDismissRequest = onDismiss,
    ) {
        LazyColumn(
            state = listState,
            modifier = Modifier.padding(20.dp)
        ) {
            items(uniqueMediaStream, key = { it.id }) { item ->
                OutlinedCard(
                    modifier = Modifier
                        .padding(vertical = 4.dp)
                ) {
                    VerticallyCenteredSingleLineText(
                        item.description,
                        modifier = Modifier
                            .height(50.dp)
                            .fillMaxWidth()
                            .clickable {
                                onResolutionSelected(item)
                                onDismiss()
                            }
                            .padding(start = 8.dp),
                    )
                }
            }
        }
    }
}
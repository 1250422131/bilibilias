package com.imcys.bilibilias.ui

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.ui.tooling.preview.Preview

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VideoDownloadDialog() {
    val listState = rememberLazyListState()
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    ModalBottomSheet(
        onDismissRequest = {},
        sheetState = sheetState,
        containerColor = MaterialTheme.colorScheme.surfaceContainer
    ) {
        Box(modifier = Modifier.fillMaxHeight(0.7f)) {
            LazyColumn(state = listState) {
                items(50, contentType = {}) {
                    Text(
                        it.toString(),
                        modifier = Modifier
                            .padding(horizontal = 16.dp, vertical = 4.dp)
                            .height(50.dp)
                            .fillMaxWidth()
                            .border(
                                1.dp,
                                MaterialTheme.colorScheme.primary,
                                RoundedCornerShape(8.dp)
                            )
                            .wrapContentHeight()
                            .padding(start = 8.dp),
                        textAlign = TextAlign.Start,
                        maxLines = 1
                    )
                }
            }
            Row(modifier = Modifier.padding(16.dp).align(Alignment.BottomCenter)) {
                Text("bottom")
            }
        }
    }
}

@Preview
@Composable
fun PreviewVideoDownloadDialog() {
    VideoDownloadDialog()
}
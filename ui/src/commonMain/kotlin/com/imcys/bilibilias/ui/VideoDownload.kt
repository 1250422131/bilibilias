package com.imcys.bilibilias.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.border
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
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.KeyboardArrowDown
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
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
import org.jetbrains.compose.ui.tooling.preview.Preview

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VideoDownloadDialog() {
    val listState = rememberLazyListState()
    var openDialog by remember { mutableStateOf(false) }
    AsModalBottomSheet(
        true,
        onDismiss = { openDialog = false },
    ) {
        Column(
            modifier = Modifier
                .padding(20.dp, 0.dp, 20.dp, 20.dp)
        ) {
            Row(
                modifier = Modifier.clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null
                ) { openDialog = true },
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text("1018P")
                Icon(Icons.Rounded.KeyboardArrowDown, null)
            }
            Box(modifier = Modifier.fillMaxHeight(0.7f)) {
                LazyColumn(state = listState) {
                    items(20, contentType = {}) {
                        Text(
                            it.toString(),
                            modifier = Modifier
                                .padding(vertical = 4.dp)
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
    FormatsDialog(openDialog)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FormatsDialog(openDialog: Boolean) {
    val listState = rememberLazyListState()
    AsModalBottomSheet(
        openDialog,
        onDismiss = { },
    ) {
        LazyColumn(state = listState) {
            items(6) {
                Text(
                    it.toString(),
                    modifier = Modifier
                        .padding(vertical = 4.dp)
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
    VideoDownloadDialog()
}
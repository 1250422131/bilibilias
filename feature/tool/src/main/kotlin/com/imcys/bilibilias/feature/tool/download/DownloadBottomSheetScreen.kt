package com.imcys.bilibilias.feature.tool.download

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Download
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.imcys.bilibilias.core.designsystem.component.AsOutlinedButton
import com.imcys.bilibilias.core.designsystem.component.FancyIndicatorContainerTabs
import com.imcys.bilibilias.core.ui.dropdownmenu.ExposedDropdownMenuSample
import com.imcys.bilibilias.feature.tool.download.DownloadBottomSheetComponent.Model
import com.imcys.bilibilias.feature.tool.download.DownloadTypeBottomSheetEvent.ChangesAuthor
import com.imcys.bilibilias.feature.tool.download.DownloadTypeBottomSheetEvent.ChangesTitle
import com.skydoves.flexible.bottomsheet.material3.FlexibleBottomSheet
import com.skydoves.flexible.core.rememberFlexibleBottomSheetState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DownloadBottomSheetScreen(component: DownloadBottomSheetComponent, onDismiss: () -> Unit) {
    val model by component.models.collectAsStateWithLifecycle()
    DownloadBottomSheetContent(
        model = model,
        onDismiss = onDismiss,
        onEvent = component::take,
        modifier = Modifier,
    )
}

@Composable
internal fun DownloadBottomSheetContent(
    model: Model,
    onDismiss: () -> Unit,
    onEvent: (DownloadTypeBottomSheetEvent) -> Unit,
    modifier: Modifier = Modifier,
) {
    val sheetState = rememberFlexibleBottomSheetState(
        containSystemBars = true,
        isModal = true,
        skipSlightlyExpanded = false,
    )
    FlexibleBottomSheet(
        sheetState = sheetState,
        onDismissRequest = onDismiss,
        dragHandle = {
            Row {
                Spacer(Modifier.weight(1f))
                AsOutlinedButton(onClick = {}) {
                    Icon(Icons.Default.Download, contentDescription = null)
                    Text("下载")
                }
            }
        },
    ) {
        DownloadTypeTabs(
            titles = listOf("音频", "视频"),
            selectedTabIndex = {},
            title = model.title,
            onChangeTitle = { onEvent(ChangesTitle(it)) },
            author = model.author,
            onChangeAuthor = { onEvent(ChangesAuthor(it)) },
        ) {
        }
    }
}

@Composable
internal fun DownloadTypeTabs(
    titles: List<String>,
    selectedTabIndex: (Int) -> Unit,
    title: String,
    onChangeTitle: (String) -> Unit,
    author: String,
    onChangeAuthor: (String) -> Unit,
    content: @Composable () -> Unit,
) {
    var state by remember { mutableIntStateOf(0) }
    FancyIndicatorContainerTabs(
        modifier = Modifier.padding(horizontal = 16.dp),
        selectedTabIndex = state,
        tabs = {
            titles.forEachIndexed { index, title ->
                Tab(
                    selected = state == index,
                    onClick = {
                        state = index
                        selectedTabIndex(state)
                    },
                    text = { Text(title) },
                )
            }
        },
    ) {
        TextField(
            value = title,
            onValueChange = onChangeTitle,
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.CenterHorizontally),
        )
        Spacer(Modifier.height(8.dp))
        Row {
            TextField(
                value = author,
                onValueChange = onChangeAuthor,
                modifier = Modifier.weight(1f),
            )
            Spacer(Modifier.width(8.dp))
            ExposedDropdownMenuSample(modifier = Modifier.weight(1f))
        }
        content()
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewDownloadBottomSheetScreen() {
    DownloadBottomSheetScreen(PreviewDownloadBottomSheetComponent(), {})
}

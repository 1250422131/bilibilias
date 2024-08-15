package com.imcys.bilibilias.feature.tool.download

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
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
import com.imcys.bilibilias.core.designsystem.component.FancyIndicatorContainerTabs
import com.imcys.bilibilias.core.ui.dropdownmenu.ExposedDropdownMenuSample

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DownloadBottomSheetScreen(downloadBottomSheetComponent: DownloadBottomSheetComponent) {
    var state by remember { mutableIntStateOf(0) }
    val titles = remember { listOf("Tab 1", "Tab 2", "Tab 3") }
    FancyIndicatorContainerTabs(
        state,
        tabs = {
            titles.forEachIndexed { index, title ->
                Tab(selected = state == index, onClick = { state = index }, text = { Text(title) })
            }
        },
    ) {
        TextField(
            value = "title",
            onValueChange = {},
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.CenterHorizontally),
        )
        Row {
            TextField(value = "作者", onValueChange = {}, modifier = Modifier.weight(1f))
            ExposedDropdownMenuSample()
        }
        Text(
            modifier = Modifier.align(Alignment.CenterHorizontally),
            text = "Fancy transition tab ${state + 1} selected",
            style = MaterialTheme.typography.bodyLarge,
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewDownloadBottomSheetScreen() {
    DownloadBottomSheetScreen(PreviewDownloadBottomSheetComponent())
}

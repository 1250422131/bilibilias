package com.imcys.bilibilias.feature.tool.download

import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.skydoves.flexible.bottomsheet.material3.FlexibleBottomSheet
import com.skydoves.flexible.core.FlexibleSheetValue
import com.skydoves.flexible.core.rememberFlexibleBottomSheetState
import kotlinx.coroutines.launch

@Composable
fun DownloadBottomSheetScreen(downloadBottomSheetComponent: DownloadBottomSheetComponent) {
    val scope = rememberCoroutineScope()
    val sheetState = rememberFlexibleBottomSheetState(
        isModal = true,
    )

    FlexibleBottomSheet(
        sheetState = sheetState,
        containerColor = Color.Black,
        onDismissRequest = {}
    ) {
        Button(
            modifier = Modifier.align(Alignment.CenterHorizontally),
            onClick = {
                scope.launch {
                    when (sheetState.swipeableState.currentValue) {
                        FlexibleSheetValue.SlightlyExpanded -> sheetState.intermediatelyExpand()
                        FlexibleSheetValue.IntermediatelyExpanded -> sheetState.fullyExpand()
                        else -> sheetState.hide()
                    }
                }
            },
        ) {
            Text(text = "Expand Or Hide")
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewDownloadBottomSheetScreen() {
    DownloadBottomSheetScreen(PreviewDownloadBottomSheetComponent())
}


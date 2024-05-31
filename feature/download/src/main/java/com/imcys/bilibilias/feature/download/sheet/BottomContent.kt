package com.imcys.bilibilias.feature.download.sheet

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.dokar.sheets.BottomSheetValue
import com.dokar.sheets.m3.BottomSheet
import com.dokar.sheets.rememberBottomSheetState
import com.imcys.bilibilias.core.designsystem.component.AsButton
import com.imcys.bilibilias.core.model.video.ViewInfo

@Composable
internal fun BottomSheetContent(
    component: DialogComponent,
    navigationToPlayer: (viewInfo: ViewInfo) -> Unit
) {
    val state = rememberBottomSheetState(
        initialValue = BottomSheetValue.Expanded,
        confirmValueChange = {
            if (it == BottomSheetValue.Collapsed) {
                component.onDismissClicked()
            }
            true
        },
    )
    BottomSheet(state = state) {
        Column(
            modifier = Modifier,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AsButton(
                onClick = {
                    component.take(DialogComponent.Event.DeleteFile)
                },
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth()
                    .height(60.dp)
            ) {
                Text(text = "删除")
            }
            AsButton(
                onClick = { navigationToPlayer(component.navigationToPlayer()) },
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth()
                    .height(60.dp)
            ) {
                Text(text = "播放")
            }
        }
    }
}

package com.imcys.bilibilias.core.designsystem.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AsModalBottomSheet(
    onDismissRequest: () -> Unit,
    sheetState: SheetState = rememberModalBottomSheetState(),
    onConfirm: () -> Unit,
    onCancel: () -> Unit,
    content: @Composable ColumnScope.() -> Unit
) {
    ModalBottomSheet(
        onDismissRequest = onDismissRequest,
        sheetState = sheetState,
        modifier = Modifier
    ) {
        Column(modifier = Modifier.padding(horizontal = 20.dp)) {
            content()
            AsButton(
                onClick = onConfirm,
                modifier = Modifier
                    .padding(top = 20.dp)
                    .height(60.dp)
                    .fillMaxWidth()
            ) {
                Text(text = "确定", fontSize = 16.sp, fontWeight = FontWeight.Bold)
            }
            Button(
                onClick = onCancel,
                modifier = Modifier
                    .padding(vertical = 20.dp)
                    .height(60.dp)
                    .fillMaxWidth(),
                colors = ButtonDefaults.buttonColors().copy(containerColor = Color(255, 210, 224))
            ) {
                Text(text = "取消", fontSize = 16.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}

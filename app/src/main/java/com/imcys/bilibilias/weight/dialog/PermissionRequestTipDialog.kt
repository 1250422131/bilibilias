package com.imcys.bilibilias.weight.dialog

import androidx.compose.material.icons.Icons
import androidx.compose.ui.res.stringResource
import androidx.compose.material.icons.outlined.WarningAmber
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.imcys.bilibilias.ui.weight.ASTextButton

@Composable
fun PermissionRequestTipDialog(
    show: Boolean,
    icon: @Composable (() -> Unit)? = {
        Icon(
            Icons.Outlined.WarningAmber,
            contentDescription = stringResource(R.string.home_text_1575)
        )
    },
    title: String = "权限请求",
    message: String,
    confirmText: String = "继续",
    dismissText: String = "取消",
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    if (show) {
        AlertDialog(
            onDismissRequest = onDismiss,
            icon = icon,
            title = { Text(title) },
            text = { Text(message) },
            confirmButton = {
                ASTextButton(onClick = onConfirm) {
                    Text(confirmText)
                }
            },
            dismissButton = {
                ASTextButton(onClick = onDismiss) {
                    Text(dismissText)
                }
            }
        )
    }
}
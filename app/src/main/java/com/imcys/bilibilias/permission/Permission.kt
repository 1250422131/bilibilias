package com.imcys.bilibilias.permission

import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

fun SnackbarHostState.showSnackBar(
    message: String? = null,
    action: String? = null,
    duration: SnackbarDuration = SnackbarDuration.Short,
    coroutineScope: CoroutineScope,
    onSnackBarAction: () -> Unit = {},
    onSnackBarDismiss: () -> Unit = {},
) {
    if (!message.isNullOrEmpty()) {
        coroutineScope.launch {
            when (
                showSnackbar(
                    message = message,
                    duration = duration,
                    actionLabel = action,
                    withDismissAction = duration == SnackbarDuration.Indefinite,
                )
            ) {
                SnackbarResult.Dismissed -> onSnackBarDismiss.invoke()
                SnackbarResult.ActionPerformed -> onSnackBarAction.invoke()
            }
        }
    }
}

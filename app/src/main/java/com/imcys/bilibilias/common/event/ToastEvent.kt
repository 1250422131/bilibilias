package com.imcys.bilibilias.common.event

import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarResult
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.runBlocking


sealed class ToastEvent(
    open val message: String,
    open val onResult: (SnackbarResult) -> Unit,
    open val duration: SnackbarDuration = SnackbarDuration.Short
) {
    // 带按钮的吐司
    data class ActionToastEvent(
        override val message: String,
        val actionLabel: String,
        override val duration: SnackbarDuration = SnackbarDuration.Indefinite,
        override val onResult: (SnackbarResult) -> Unit = {}
    ) : ToastEvent(message, onResult, duration)

    // 普通吐司
    data class NormalToastEvent(
        override val message: String,
        override val duration: SnackbarDuration = SnackbarDuration.Short,
        override val onResult: (SnackbarResult) -> Unit = {}
    ) : ToastEvent(message, onResult, duration)
}

// 消息管道
private val toastEventChannel = Channel<ToastEvent>(Channel.UNLIMITED)
val toastEventFlow = toastEventChannel.receiveAsFlow()

suspend fun sendToastEvent(
    message: String,
    actionLabel: String? = null,
    duration: SnackbarDuration = SnackbarDuration.Short,
    onResult: (SnackbarResult) -> Unit = {}
) {
    if (actionLabel != null) {
        toastEventChannel.send(
            ToastEvent.ActionToastEvent(
                message = message,
                actionLabel = actionLabel,
                duration = duration,
                onResult = onResult
            )
        )
    } else {
        toastEventChannel.send(
            ToastEvent.NormalToastEvent(
                message = message,
                duration = duration,
                onResult = onResult
            )
        )
    }
}

fun sendToastEventOnBlocking(
    message: String,
    actionLabel: String? = null,
    duration: SnackbarDuration = SnackbarDuration.Short,
    onResult: (SnackbarResult) -> Unit = {}
) {
    runBlocking {
        sendToastEvent(
            message = message,
            actionLabel = actionLabel,
            duration = duration,
            onResult = onResult
        )
    }
}


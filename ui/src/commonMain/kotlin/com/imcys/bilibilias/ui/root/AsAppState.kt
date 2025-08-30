package com.imcys.bilibilias.ui.root

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import com.imcys.bilibilias.core.data.model.MessageData
import com.imcys.bilibilias.core.data.util.ErrorMonitor
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

@Composable
fun rememberAsAppState(
    errorMonitor: ErrorMonitor,
    coroutineScope: CoroutineScope = rememberCoroutineScope(),
): AsAppState {
    return remember(
        coroutineScope,
        errorMonitor,
    ) {
        AsAppState(
            coroutineScope = coroutineScope,
            errorMonitor = errorMonitor,
        )
    }
}

@Stable
class AsAppState(
    coroutineScope: CoroutineScope,
    val errorMonitor: ErrorMonitor,
) {
    val stateMessage: StateFlow<MessageData?> = errorMonitor.messages.map {
        it.firstOrNull()
    }.stateIn(
        scope = coroutineScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = null,
    )
}
package com.imcys.bilibilias.core.data.toast

import kotlinx.coroutines.flow.Flow
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds

interface ToastMachine {
    val message: Flow<AsToastState?>
    fun show(toastState: AsToastState)
    fun show(text: String)
}

data class AsToastState(
    val message: String,
    val type: AsToastType = AsToastType.Normal,
    val duration: Duration = 4000.milliseconds,
)

enum class AsToastType {
    Normal,
    Success,
    Info,
    Warning,
    Error,
}

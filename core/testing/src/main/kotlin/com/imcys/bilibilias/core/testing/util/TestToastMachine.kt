package com.imcys.bilibilias.core.testing.util

import com.imcys.bilibilias.core.data.toast.AsToastState
import com.imcys.bilibilias.core.data.toast.ToastMachine
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

class TestToastMachine : ToastMachine {

    private val messageFlow = MutableStateFlow<AsToastState?>(null)

    override val message: Flow<AsToastState?> = messageFlow

    override fun show(toastState: AsToastState) {
        messageFlow.value = toastState
    }

    override fun show(text: String) {
        messageFlow.value = AsToastState(text)
    }
}

package com.imcys.bilibilias.core.data.toast

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

class ToastMachineImpl @Inject constructor() : ToastMachine {
    private val textFlow = MutableStateFlow<AsToastState?>(null)
    override val message: Flow<AsToastState?>
        get() = textFlow.asStateFlow()

    override fun show(toastState: AsToastState) {
        textFlow.update { toastState }
    }

    override fun show(text: String) {
        show(AsToastState(text))
    }
}

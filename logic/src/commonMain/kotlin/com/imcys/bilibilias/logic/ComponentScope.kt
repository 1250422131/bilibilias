package com.imcys.bilibilias.logic

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.essenty.lifecycle.doOnDestroy
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

internal fun ComponentContext.launch(block: suspend () -> Unit) {
    lifecycle.doOnDestroy {
        this.scope.cancel()
    }
    this.scope.launch {
        block()
    }
}

internal val ComponentContext.scope: CoroutineScope
    get() = CoroutineScope(Dispatchers.Default + SupervisorJob())
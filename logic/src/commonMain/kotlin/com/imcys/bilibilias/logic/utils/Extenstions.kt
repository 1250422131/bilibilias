package com.imcys.bilibilias.logic.utils

import com.arkivanov.decompose.GenericComponentContext
import com.arkivanov.essenty.lifecycle.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

context(componentContext: GenericComponentContext<*>)
fun <T> Flow<T>.asStateFlowInLifecycle(
    initialValue: T,
    started: SharingStarted = SharingStarted.WhileSubscribed(5_000)
): StateFlow<T> {
    val lifecycleScope = componentContext.coroutineScope()
    return stateIn(lifecycleScope, started, initialValue)
}
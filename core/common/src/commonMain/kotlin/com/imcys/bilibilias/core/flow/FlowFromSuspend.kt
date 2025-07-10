package com.imcys.bilibilias.core.flow

import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector

public fun <T> flowFromSuspend(function: suspend () -> T): Flow<T> =
    FlowFromSuspend(function)

private class FlowFromSuspend<T>(private val function: suspend () -> T) : Flow<T> {
    override suspend fun collect(collector: FlowCollector<T>) {
        val value = function()
        currentCoroutineContext().ensureActive()
        collector.emit(value)
    }
}
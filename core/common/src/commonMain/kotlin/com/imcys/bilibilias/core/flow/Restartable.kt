package com.imcys.bilibilias.core.flow

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.update

class FlowRestarter {
    internal val id = MutableStateFlow(0)


    fun restart() {
        id.update { it + 1 }
    }
}

fun <T> Flow<T>.restartable(restarter: FlowRestarter): Flow<T> = restarter.id.flatMapLatest { this }

package com.imcys.bilibilias.core.coroutines

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.concurrent.atomics.ExperimentalAtomicApi
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext
import kotlin.coroutines.cancellation.CancellationException

@OptIn(ExperimentalAtomicApi::class)
class MonoTasker(
    private val scope: CoroutineScope
) {
    private var job: Job? = null

    val isRunning: Boolean
        get() = job?.isActive == true

    fun launch(
        context: CoroutineContext = EmptyCoroutineContext,
        start: CoroutineStart = CoroutineStart.DEFAULT,
        block: suspend CoroutineScope.() -> Unit
    ): Job {
        job?.cancel()
        return scope.launch(context, start, block)
            .apply {
                invokeOnCompletion {
                    job = null
                }
            }
            .also {
                job = it
            }
    }


    fun cancel(cause: CancellationException?) {
        job?.cancel()
    }

    suspend fun join() {
        job?.join()
    }
}
package com.imcys.bilibilias.common.base.extend

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

fun CoroutineScope.launchIO(
    start: CoroutineStart = CoroutineStart.DEFAULT,
    block: suspend CoroutineScope.() -> Unit,
): Job {
    return this.launch(Dispatchers.IO, start, block)
}

 fun CoroutineScope.launchUI(
    start: CoroutineStart = CoroutineStart.DEFAULT,
    block: suspend CoroutineScope.() -> Unit,
): Job {
    return this.launch(Dispatchers.Main, start, block)
}

fun launchUI(
    block: suspend CoroutineScope.() -> Unit,
): Job {
    return CoroutineScope(Dispatchers.Main).launchUI { block.invoke(this) }
}

fun launchIO(
    block: suspend CoroutineScope.() -> Unit,
): Job {
    return CoroutineScope(Dispatchers.IO).launchIO { block.invoke(this) }
}

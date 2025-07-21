package com.imcys.bilibilias.core.coroutines

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob

object AsDispatchers {
    val ApplicationScope: CoroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.Default)
    val IO: CoroutineDispatcher = Dispatchers.IO
    val Default: CoroutineDispatcher = Dispatchers.Default
}
package com.imcys.bilibilias

import com.imcys.bilibilias.core.startup.Startup
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class StartupSet(
    private val applicationScope: CoroutineScope,
) {
    private val set = mutableSetOf<Startup>()
    fun add(startup: Startup) {
        set += startup
    }

    fun start() {
        applicationScope.launch {
            set.forEach {
                it.initialize()
            }
        }
    }
}
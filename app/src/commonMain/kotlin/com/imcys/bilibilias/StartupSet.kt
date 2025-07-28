package com.imcys.bilibilias

import com.imcys.bilibilias.core.data.DataStoreProvider
import com.imcys.bilibilias.core.datasource.utils.WbiInitializer
import com.imcys.bilibilias.core.startup.Startup
import com.imcys.bilibilias.startup.StartupHttpDownloader
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class StartupSet(
    private val applicationScope: CoroutineScope,
) {
    private val set = mutableSetOf<Startup>()
    fun add(startup: Startup) = apply {
        set += startup
    }

    fun start() {
        applicationScope.launch {
            set.forEach {
                it.initialize()
            }
        }
    }

    companion object {
        fun create(applicationScope: CoroutineScope) {
            StartupSet(applicationScope)
                .add(WbiInitializer())
                .add(StartupHttpDownloader(DataStoreProvider.httpDownloader))
                .start()
        }
    }
}
package com.imcys.bilibilias.core.di

import co.touchlab.kermit.Logger
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import org.koin.core.qualifier.named
import org.koin.dsl.module

val coroutineScopeModule = module {
    single {
        val dispatcher: CoroutineDispatcher = get(named(AsDispatchers.DEFAULT))
        CoroutineScope(
            CoroutineExceptionHandler { coroutineContext, throwable ->
                Logger.w("ApplicationScope", throwable) {
                    "Uncaught exception in coroutine $coroutineContext"
                }
            } + SupervisorJob() + dispatcher,
        )
    }
}
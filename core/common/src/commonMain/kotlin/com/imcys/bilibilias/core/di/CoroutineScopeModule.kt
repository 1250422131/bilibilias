package com.imcys.bilibilias.core.di

import co.touchlab.kermit.Logger
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import org.koin.core.scope.Scope
import org.koin.dsl.module

val coroutineScopeModule = module {
    single {
        CoroutineScope(
            CoroutineExceptionHandler { coroutineContext, throwable ->
                Logger.w("ApplicationScope", throwable) {
                    "Uncaught exception in coroutine $coroutineContext"
                }
            } + SupervisorJob() + IO,
        )
    }
}

val Scope.applicationScope: CoroutineScope
    get() = get()
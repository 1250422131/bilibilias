package com.imcys.bilibilias.core.di

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import org.koin.core.qualifier.named
import org.koin.core.scope.Scope
import org.koin.dsl.module

val coroutineDispatcherModule = module {
    single<CoroutineDispatcher>(ioDispatcher) { Dispatchers.IO }
    single<CoroutineDispatcher>(defaultDispatcher) { Dispatchers.Default }
    single<CoroutineDispatcher>(mainDispatcher) { Dispatchers.Main }
}
val Scope.IO: CoroutineDispatcher
    get() = get<CoroutineDispatcher>(ioDispatcher)
val Scope.Default: CoroutineDispatcher
    get() = get<CoroutineDispatcher>(defaultDispatcher)
val Scope.Main: CoroutineDispatcher
    get() = get<CoroutineDispatcher>(mainDispatcher)

val ioDispatcher = named("IODispatcher")
val defaultDispatcher = named("DefaultDispatcher")
val mainDispatcher = named("MainDispatcher")
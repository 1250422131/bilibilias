package com.imcys.bilibilias.core.di

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import org.koin.core.qualifier.named
import org.koin.dsl.module

val coroutineDispatcherModule = module {
    single<CoroutineDispatcher>(named(AsDispatchers.IO)) { Dispatchers.IO }
    single<CoroutineDispatcher>(named(AsDispatchers.DEFAULT)) { Dispatchers.Default }
    single<CoroutineDispatcher>(named(AsDispatchers.MAIN)) { Dispatchers.Main }
}

enum class AsDispatchers {
    IO,
    DEFAULT,
    MAIN,
}


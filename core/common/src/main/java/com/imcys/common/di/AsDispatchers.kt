package com.imcys.common.di

import javax.inject.Qualifier
import kotlin.annotation.AnnotationRetention.RUNTIME

@Qualifier
@Retention(RUNTIME)
annotation class Dispatcher(val asDispatcher: AsDispatchers)

enum class AsDispatchers {
    Default,
    IO,
}

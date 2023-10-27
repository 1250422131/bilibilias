package com.imcys.bilibilias.common.di

import javax.inject.Qualifier
import kotlin.annotation.AnnotationRetention.BINARY

@Qualifier
@Retention(BINARY)
annotation class Dispatcher(val dispatcher: AsDispatchers)

enum class AsDispatchers {
    Default, IO
}

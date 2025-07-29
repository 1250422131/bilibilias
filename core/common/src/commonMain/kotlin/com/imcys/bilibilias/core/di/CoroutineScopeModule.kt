package com.imcys.bilibilias.core.di

import kotlinx.coroutines.CoroutineScope
import org.koin.core.scope.Scope

val Scope.applicationScope: CoroutineScope
    get() = get()
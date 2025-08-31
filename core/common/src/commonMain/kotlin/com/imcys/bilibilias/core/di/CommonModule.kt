package com.imcys.bilibilias.core.di

import kotlinx.coroutines.CoroutineScope
import org.koin.core.module.Module
import org.koin.core.scope.Scope

expect val CommonModule: Module
val Scope.applicationScope: CoroutineScope
    get() = get()
package com.imcys.bilibilias.core.di

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import org.koin.core.qualifier.Qualifier
import org.koin.core.qualifier.QualifierValue
import org.koin.core.scope.Scope
import org.koin.dsl.module

val IOQualifier = DispatchersQualifier("IODispatcher")
val DefaultQualifier = DispatchersQualifier("DefaultDispatcher")
val MainQualifier = DispatchersQualifier("MainDispatcher")
val coroutineDispatcherModule = module {
    single<CoroutineDispatcher>(IOQualifier) { Dispatchers.IO }
    single<CoroutineDispatcher>(DefaultQualifier) { Dispatchers.Default }
    single<CoroutineDispatcher>(MainQualifier) { Dispatchers.Main }
}
val Scope.IO: CoroutineDispatcher
    get() = get<CoroutineDispatcher>(IOQualifier)
val Scope.Default: CoroutineDispatcher
    get() = get<CoroutineDispatcher>(DefaultQualifier)
val Scope.Main: CoroutineDispatcher
    get() = get<CoroutineDispatcher>(MainQualifier)

@JvmInline
value class DispatchersQualifier(override val value: QualifierValue) : Qualifier {
    override fun toString(): String = value
}
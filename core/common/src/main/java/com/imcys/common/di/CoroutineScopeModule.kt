package com.imcys.common.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import javax.inject.Qualifier
import javax.inject.Singleton

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class AppCoroutineScope

@Module
@InstallIn(SingletonComponent::class)
class CoroutineScopeModule {
    @Provides
    @Singleton
    @AppCoroutineScope
    fun provideAppCoroutineScope(@Dispatcher(AsDispatchers.IO) ioDispatch: CoroutineDispatcher): CoroutineScope =
        CoroutineScope(
            SupervisorJob() + ioDispatch + CoroutineExceptionHandler { _, throwable ->
                throwable.printStackTrace()
            }
        )
}

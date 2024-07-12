package com.imcys.bilibilias.core.common.utils.di

import com.imcys.bilibilias.core.common.utils.ExecutorUtil
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import java.util.concurrent.ExecutorService
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class CommonModule {
    @Provides
    @Singleton
    fun provideExecutorService(): ExecutorService = ExecutorUtil.executorService
}

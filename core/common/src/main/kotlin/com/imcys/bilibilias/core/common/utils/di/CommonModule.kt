package com.imcys.bilibilias.core.common.utils.di

import com.imcys.bilibilias.core.common.network.AsDispatchers
import com.imcys.bilibilias.core.common.network.Dispatcher
import com.imcys.bilibilias.core.common.utils.asNonTerminatingExecutorService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.asExecutor
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import java.util.concurrent.ExecutorService
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class CommonModule {
    @Provides
    @Singleton
    fun provideExecutorService(
        @Dispatcher(AsDispatchers.IO) ioDispatch: CoroutineDispatcher
    ): ExecutorService =
        ioDispatch.asExecutor().asNonTerminatingExecutorService()

    @OptIn(ExperimentalSerializationApi::class)
    @Provides
    @Singleton
    fun provideJson(): Json = Json {
        prettyPrint = true
        ignoreUnknownKeys = true
        // 使用默认值覆盖 null
        coerceInputValues = true
        prettyPrintIndent = "  "
    }
}

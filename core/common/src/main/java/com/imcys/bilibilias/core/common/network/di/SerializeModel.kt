package com.imcys.bilibilias.core.common.network.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class SerializeModel {
    @Provides
    @Singleton
    fun provideJson(): Json = Json {
        prettyPrint = true
        ignoreUnknownKeys = true
        // 使用默认值覆盖 null
        coerceInputValues = true
    }
}

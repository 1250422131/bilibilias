package com.imcys.common.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.cbor.Cbor
import kotlinx.serialization.json.Json
import kotlinx.serialization.protobuf.ProtoBuf
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class SerializeModel {
    @Provides
    @Singleton
    fun provideJson(): Json = Json {
        prettyPrint = true
        isLenient = true
        ignoreUnknownKeys = true
        allowSpecialFloatingPointValues = true
        allowStructuredMapKeys = true
    }

    @OptIn(ExperimentalSerializationApi::class)
    @Provides
    @Singleton
    fun provideCbor(): Cbor = Cbor
    @OptIn(ExperimentalSerializationApi::class)
    @Provides
    @Singleton
    fun provideProto(): ProtoBuf = ProtoBuf
}
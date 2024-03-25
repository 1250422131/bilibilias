package com.imcys.bilibilias.core.network.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.ktor.client.HttpClient
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.plugins.api.ClientPlugin
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import javax.inject.Singleton
import io.ktor.client.call.*
import io.ktor.client.plugins.defaultRequest

@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {

    @Provides
    @Singleton
    fun provideHttpClient(
        httpClientEngine: HttpClientEngine,
        json: Json,
        transform: ClientPlugin<Unit>,
    ): HttpClient {
        val client = HttpClient {
            defaultRequest {
                url(API_BILIBILI)
            }
        }
        return client
    }
    }
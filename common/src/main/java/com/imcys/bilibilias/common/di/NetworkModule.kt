package com.imcys.bilibilias.common.di

import com.imcys.bilibilias.common.base.constant.ROAM_API
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.HttpRequestRetry
import io.ktor.client.plugins.HttpSend
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.cookies.HttpCookies
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.plugin
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import okhttp3.OkHttpClient
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {
    @Provides
    @Singleton
    fun provideHttpClient(
        asLogger: AsLogger,
        json: Json,
        asCookiesStorage: AsCookiesStorage,
        okHttpClient: OkHttpClient
    ): HttpClient = HttpClient(OkHttp.create { preconfigured = okHttpClient }) {
        install(HttpCookies) {
            storage = asCookiesStorage
        }

        install(HttpTimeout) {
            requestTimeoutMillis = 50000
        }

        defaultRequest {
            url(ROAM_API)
        }

        install(ContentNegotiation) {
            json(json)
        }

        install(HttpRequestRetry) {
            retryOnServerErrors(maxRetries = 5)
            exponentialDelay()
        }
        install(Logging) {
            logger = asLogger
            level = LogLevel.ALL
        }
    }.apply {
        plugin(HttpSend).intercept { request ->
            val originalCall = execute(request)
            if (originalCall.response.status.value !in 100..399) {
                execute(request)
            } else {
                originalCall
            }
        }
    }
}

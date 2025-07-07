package com.imcys.bilibilias.core.datasource

import io.ktor.client.HttpClient
import io.ktor.client.HttpClientConfig
import io.ktor.client.plugins.BrowserUserAgent
import io.ktor.client.plugins.HttpRedirect
import io.ktor.client.plugins.HttpRequestRetry
import io.ktor.client.plugins.HttpTimeout
import kotlinx.io.IOException

fun createHttpClient(
    block: HttpClientConfig<*>.() -> Unit = {}
): HttpClient {
    return HttpClient {
        install(HttpRequestRetry) {
            maxRetries = 1
            delayMillis { 1000 }
            retryIf { cause, response ->
                // 只重试网络异常
                cause is IOException
            }
        }
        install(HttpTimeout) {
            requestTimeoutMillis = 300_000
            connectTimeoutMillis = 30_000
            socketTimeoutMillis = 30_000
        }
        BrowserUserAgent()
        followRedirects = true
        install(HttpRedirect) {
            allowHttpsDowngrade = true
        }
        // All clients actually expect success by default in clientConfig, so we move them here
        expectSuccess = true
        block()
    }
}
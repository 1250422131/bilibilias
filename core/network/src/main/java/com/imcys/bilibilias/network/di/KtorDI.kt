package com.imcys.bilibilias.network.di

import android.util.Log
import com.imcys.bilibilias.network.AsCookiesStorage
import com.imcys.bilibilias.network.config.BROWSER_USER_AGENT
import com.imcys.bilibilias.network.service.BILIBILITVAPIService
import com.imcys.bilibilias.network.service.BILIBILIWebAPIService
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.HttpRequestRetry
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.cookies.HttpCookies
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import okhttp3.OkHttpClient
import org.koin.dsl.module
import org.slf4j.LoggerFactory
import java.util.concurrent.TimeUnit
import io.ktor.client.plugins.logging.*
import io.ktor.http.HttpHeaders


val netWorkModule = module {
    single {
        Json {
            prettyPrint = true
            ignoreUnknownKeys = true
            isLenient = true
            coerceInputValues = true
            allowSpecialFloatingPointValues = true
            explicitNulls = false
        }
    }

    single {
        OkHttpClient.Builder()
            .pingInterval(1, TimeUnit.SECONDS)
            .addInterceptor { chain ->
                val request = chain.request().newBuilder()
                    .header(HttpHeaders.UserAgent, BROWSER_USER_AGENT)
                    .build()
                chain.proceed(request)
            }
            .build()
    }

    @OptIn(ExperimentalSerializationApi::class)
    single {
        AsCookiesStorage(get(),get())
    }

    single {
        HttpClient(OkHttp.create {
            preconfigured  = get<OkHttpClient>()
        }) {
            install(HttpTimeout) {
                requestTimeoutMillis = 10000
            }
            install(ContentNegotiation) {
                json(get())
            }
            install(HttpRequestRetry) {
                retryOnServerErrors(maxRetries = 5)
                exponentialDelay()
            }
            install(HttpCookies) {
                storage = get<AsCookiesStorage>()
            }
            install(Logging) {
                logger = object : Logger {
                    override fun log(message: String) {
                        Log.d("Ktor", "$message")
                    }
                }
                level = LogLevel.ALL
            }
        }
    }

    // WebAPI
    single {
        BILIBILIWebAPIService(get())
    }
    // TvAPI
    single {
        BILIBILITVAPIService(get())
    }

}



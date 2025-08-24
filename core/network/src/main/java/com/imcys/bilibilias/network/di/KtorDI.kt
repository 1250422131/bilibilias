package com.imcys.bilibilias.network.di

import android.util.Log
import com.imcys.bilibilias.datastore.userAppSettingsStore
import com.imcys.bilibilias.network.AsCookiesStorage
import com.imcys.bilibilias.network.config.BILIBILI_URL
import com.imcys.bilibilias.network.config.REFERER
import com.imcys.bilibilias.network.plugin.RiskControlPlugin
import com.imcys.bilibilias.network.plugin.RoamPlugin
import com.imcys.bilibilias.network.service.BILIBILITVAPIService
import com.imcys.bilibilias.network.service.BILIBILIWebAPIService
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.BrowserUserAgent
import io.ktor.client.plugins.HttpRequestRetry
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.cookies.HttpCookies
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement
import okhttp3.OkHttpClient
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import java.util.concurrent.TimeUnit


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
                    .apply {
                        if (!chain.request().headers("Referer").isNotEmpty()) {
                            header(REFERER, BILIBILI_URL)
                        }
                    }
                    .build()
                chain.proceed(request)
            }
            .build()
    }

    @OptIn(ExperimentalSerializationApi::class)
    single {
        AsCookiesStorage(get(), get())
    }

    single {
        HttpClient(OkHttp.create {
            preconfigured = get<OkHttpClient>()
        }) {
            BrowserUserAgent()
            install(HttpTimeout) {
                requestTimeoutMillis = 10000
            }
            install(RoamPlugin) {
                domainReplacement = mapOf(
                    "api.bilibili.com" to "bili-api.misakamoe.com",
                )
                biliUsersDao = get()
                appSetting = androidContext().userAppSettingsStore
            }
            install(RiskControlPlugin)
            install(ContentNegotiation) {
                json(get())
            }
            install(HttpRequestRetry) {
                retryOnServerErrors(maxRetries = 3)
                exponentialDelay()
            }
            install(HttpCookies) {
                storage = get<AsCookiesStorage>()
            }
            install(Logging) {
                logger = object : Logger {
                    private val json: Json = get()

                    override fun log(message: String) {
                        val formattedMessage = try {
                            if (message.contains("{") && message.contains("}")) {
                                val jsonStart = message.indexOf("{")
                                val jsonEnd = message.lastIndexOf("}") + 1
                                val prefix = message.substring(0, jsonStart)
                                val jsonBody = message.substring(jsonStart, jsonEnd)
                                val suffix = message.substring(jsonEnd)

                                // 解析并重新格式化 JSON
                                val jsonElement = json.parseToJsonElement(jsonBody)
                                val formattedJson =
                                    json.encodeToString(JsonElement.serializer(), jsonElement)

                                "$prefix\n$formattedJson$suffix"
                            } else {
                                message
                            }
                        } catch (e: Exception) {
                            message
                        }

                        Log.d("Ktor", formattedMessage)
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



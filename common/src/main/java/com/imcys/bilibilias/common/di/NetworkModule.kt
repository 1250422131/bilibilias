package com.imcys.bilibilias.common.di

import android.content.Context
import com.imcys.bilibilias.base.utils.asLogD
import com.imcys.bilibilias.common.base.api.BiliBiliAsApi
import com.imcys.bilibilias.common.base.app.BaseApplication
import com.imcys.bilibilias.common.base.constant.BILIBILI_URL
import com.imcys.bilibilias.common.base.constant.COOKIE
import com.imcys.bilibilias.common.base.utils.file.SystemUtil
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import github.leavesczy.monitor.MonitorInterceptor
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.BrowserUserAgent
import io.ktor.client.plugins.DefaultRequest
import io.ktor.client.plugins.HttpRequestRetry
import io.ktor.client.plugins.HttpSend
import io.ktor.client.plugins.api.createClientPlugin
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.cookies.HttpCookies
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.observer.ResponseObserver
import io.ktor.client.plugins.plugin
import io.ktor.client.request.header
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpMethod
import io.ktor.http.userAgent
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.intOrNull
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import kotlinx.serialization.serializer
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {

    @Provides
    @Singleton
    fun provideHttpClient(@ApplicationContext appContext: Context): HttpClient = HttpClient(
        OkHttp.create {
            addInterceptor(MonitorInterceptor(appContext))
        }
    ) {
        defaultRequest {
            url(BILIBILI_URL)
        }
        BrowserUserAgent()
        install(HttpCookies)
        install(convertPlugin)
        install(DefaultRequest) {
            header(HttpHeaders.ContentType, ContentType.Application.Json)
        }
        install(ResponseObserver) {
            onResponse { response ->
                asLogD("Http status:", "${response.status.value}")
            }
        }
        install(ContentNegotiation) {
            json(
                Json {
                    prettyPrint = true
                    isLenient = true
                    ignoreUnknownKeys = true
                }
            )
        }
        install(HttpRequestRetry) {
            retryOnServerErrors(maxRetries = 2)
            exponentialDelay()
        }
        install(Logging) {
            logger = object : Logger {
                override fun log(message: String) {
                    asLogD("Plugin", message)
                }
            }
            level = LogLevel.ALL
            filter { request ->
                request.url.host.contains("ktor.io")
            }
            sanitizeHeader { header -> header == HttpHeaders.Authorization }
        }
    }.apply {
        plugin(HttpSend).intercept { request ->
            if (request.headers.contains("misakamoe")) {
                request.userAgent(SystemUtil.getUserAgent() + " BILIBILIAS/${BiliBiliAsApi.version}")
            }
            // todo 注意是否还有其他方法使用cookie
            if (request.method == HttpMethod.Get) {
                request.header(COOKIE, BaseApplication.asUser.cookie)
            }

            val originalCall = execute(request)
            if (originalCall.response.status.value !in 100..399) {
                execute(request)
            } else {
                originalCall
            }
        }
    }

    @OptIn(InternalSerializationApi::class)
    private val convertPlugin =
        createClientPlugin("ConvertPlugin") {
            transformResponseBody { response, content, requestedType ->
                val jsonObject = Json.parseToJsonElement(response.bodyAsText()).jsonObject
                val code = jsonObject["code"]?.jsonPrimitive?.intOrNull
                if (code == 0) {
                    val jsonElement = jsonObject["data"]
                    val obj = jsonElement?.jsonObject
                }
                val type = requestedType.type.serializer()
                Json.decodeFromJsonElement(type, jsonObject["data"]!!)
            }
        }
}

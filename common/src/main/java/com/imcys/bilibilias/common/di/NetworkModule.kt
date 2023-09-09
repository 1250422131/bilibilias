package com.imcys.bilibilias.common.di

import android.content.Context
import com.imcys.bilibilias.base.utils.asLogD
import com.imcys.bilibilias.common.base.api.BiliBiliAsApi
import com.imcys.bilibilias.common.base.config.CacheManager
import com.imcys.bilibilias.common.base.config.CookieManager
import com.imcys.bilibilias.common.base.config.LoggerManager
import com.imcys.bilibilias.common.base.constant.ROAM_API
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
import io.ktor.client.plugins.Charsets
import io.ktor.client.plugins.DefaultRequest
import io.ktor.client.plugins.HttpRequestRetry
import io.ktor.client.plugins.HttpSend
import io.ktor.client.plugins.api.createClientPlugin
import io.ktor.client.plugins.cache.HttpCache
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.cookies.HttpCookies
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.observer.ResponseObserver
import io.ktor.client.plugins.plugin
import io.ktor.client.request.header
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.userAgent
import io.ktor.serialization.kotlinx.json.json
import io.ktor.utils.io.errors.IOException
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.contentOrNull
import kotlinx.serialization.json.intOrNull
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import kotlinx.serialization.serializer
import okhttp3.CookieJar
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {

    @Provides
    @Singleton
    fun provideHttpClient(@ApplicationContext appContext: Context): HttpClient = HttpClient(
        OkHttp.create {
            addInterceptor(MonitorInterceptor(appContext))
            config {
                cookieJar(CookieJar.NO_COOKIES)
            }
        }
    ) {
        Charsets {
            register(Charsets.UTF_8)
        }
        defaultRequest {
            url(ROAM_API)
        }
        BrowserUserAgent()
        install(HttpCookies) {
            storage = CookieManager()
        }
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
            logger = LoggerManager()
            level = LogLevel.ALL
        }
        install(HttpCache) {
            publicStorage(CacheManager())
        }
    }.apply {
        plugin(HttpSend).intercept { request ->
            if (request.headers.contains("misakamoe")) {
                request.userAgent(SystemUtil.getUserAgent() + " BILIBILIAS/${BiliBiliAsApi.version}")
            }

            val originalCall = execute(request)
            if (originalCall.response.status.value !in 100..399) {
                execute(request)
            } else {
                originalCall
            }
        }
    }

    private val json = Json {
        ignoreUnknownKeys = true
    }

    @OptIn(InternalSerializationApi::class)
    private val convertPlugin =
        createClientPlugin("ConvertPlugin") {
            transformResponseBody { response, content, requestedType ->
                try {
                    val res = Json.parseToJsonElement(response.bodyAsText()).jsonObject
                    val code = res["code"]?.jsonPrimitive?.intOrNull
                    if (code != SUCCESS) {
                        val message = res["message"]?.jsonPrimitive?.contentOrNull ?: throw ApiIOException("发生未知服务器异常")
                        throw ApiIOException(message)
                    }
                    val data = res["data"]?.jsonObject ?: throw NullResponseDataIOException()
                    val type = requestedType.type
                    json.decodeFromJsonElement(type.serializer(), data)
                } catch (e: Exception) {
                    // todo {"code":-101,"message":"账号未登录","ttl":1}
                    throw IOException(e.localizedMessage)
                }
            }
        }

    companion object {
        private const val SUCCESS = 0
    }

    internal class ApiIOException(errorMessage: String?) : IOException(errorMessage)
    internal class NullResponseDataIOException : Exception()
}

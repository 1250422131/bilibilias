package com.imcys.bilibilias.common.di

import android.content.Context
import com.imcys.bilibilias.common.base.api.BiliBiliAsApi
import com.imcys.bilibilias.common.base.config.CacheManager
import com.imcys.bilibilias.common.base.config.CookieManager
import com.imcys.bilibilias.common.base.config.LoggerManager
import com.imcys.bilibilias.common.base.constant.ROAM_API
import com.imcys.bilibilias.common.base.utils.asLogD
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
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.contentOrNull
import kotlinx.serialization.json.intOrNull
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import kotlinx.serialization.serializer
import okhttp3.CookieJar
import timber.log.Timber
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
            level = LogLevel.BODY
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
                        val message = res["message"]?.jsonPrimitive?.contentOrNull
                        throw ApiIOException(message)
                    }
                    var realData = res["data"] ?: throw NullResponseDataIOException()
                    when (realData) {
                        is JsonObject -> {
                            realData = realData.jsonObject
                            val type = requestedType.type.serializer()
                            json.decodeFromJsonElement(type, realData)
                        }

                        is JsonArray -> {
                            realData = realData.jsonArray
                            Timber.tag("ConvertPlugin").d("data=$realData")
                            val type = requestedType.kotlinType ?: throw NoTypeException(requestedType.toString())
                            json.decodeFromJsonElement(serializer(type), realData)
                        }

                        else -> null
                    }
                } catch (e: Exception) {
                    Timber.tag("ConvertPlugin").e(e)
                    null
                }
            }
        }

    companion object {
        private const val SUCCESS = 0
    }

    internal class ApiIOException(errorMessage: String?) : IOException(errorMessage)
    internal class NullResponseDataIOException : Exception()
    internal class NoTypeException(msg: String) : Exception(msg)
}

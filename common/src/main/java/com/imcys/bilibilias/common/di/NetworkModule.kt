package com.imcys.bilibilias.common.di

import android.content.Context
import com.imcys.bilibilias.common.base.api.BiliBiliAsApi
import com.imcys.bilibilias.common.base.config.CacheManager
import com.imcys.bilibilias.common.base.config.CookieManager
import com.imcys.bilibilias.common.base.config.LoggerManager
import com.imcys.bilibilias.common.base.constant.ROAM_API
import com.imcys.bilibilias.common.base.extend.ofMap
import com.imcys.bilibilias.common.base.extend.print
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
import io.ktor.client.plugins.compression.*
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.cookies.HttpCookies
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.observer.ResponseObserver
import io.ktor.client.plugins.plugin
import io.ktor.client.statement.bodyAsText
import io.ktor.http.userAgent
import io.ktor.serialization.kotlinx.json.json
import io.ktor.utils.io.ByteReadChannel
import io.ktor.utils.io.errors.IOException
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.contentOrNull
import kotlinx.serialization.json.intOrNull
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import kotlinx.serialization.serializer
import timber.log.Timber
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {

    @OptIn(ExperimentalSerializationApi::class)
    private val json = Json {
        prettyPrint = true
        isLenient = true
        ignoreUnknownKeys = true
        explicitNulls = true
    }

    @Provides
    @Singleton
    fun provideHttpClient(@ApplicationContext appContext: Context): HttpClient = HttpClient(
        OkHttp.create {
            addInterceptor(MonitorInterceptor(appContext))
        }
    ) {
        Charsets {
            register(Charsets.UTF_8)
        }
        defaultRequest {
            url(ROAM_API)
        }
        BrowserUserAgent()
        install(ContentEncoding) {
            deflate()
            gzip()
            identity()
        }
        install(HttpCookies) {
            storage = CookieManager()
        }
        install(convertPlugin)
        install(DefaultRequest) {
            // header(HttpHeaders.ContentType, ContentType.Application.Json)
        }
        install(ResponseObserver) {
            onResponse { response ->
                asLogD("Http status:", "${response.status.value}")
            }
        }
        install(ContentNegotiation) {
            json(json)
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

    @OptIn(ExperimentalSerializationApi::class)
    private val convertPlugin =
        createClientPlugin("TransformData") {
            transformResponseBody { response, content, requestedType ->
                try {
                    Timber.tag("TransformData").d("type=$requestedType")
                    if (requestedType.type == ByteReadChannel::class) return@transformResponseBody null
                    /**
                     * 实验性使用
                     */
                    // json.decodeFromStream(serializer(requestedType.reifiedType), response.readBytes().inputStream())
                    val rep = response.bodyAsText()
                    val res = json.parseToJsonElement(rep).jsonObject
                    val code = res["code"]?.jsonPrimitive?.intOrNull
                    if (code != SUCCESS) {
                        val message = res["message"]?.jsonPrimitive?.contentOrNull
                        throw ApiIOException(message)
                    }
                    var realData = res["data"] ?: throw NullResponseDataIOException()
                    when (realData) {
                        is JsonObject -> {
                            realData = realData.jsonObject
                            json.parseToJsonElement(realData.toString())
                            val element = json.decodeFromJsonElement(serializer(requestedType.reifiedType), realData)
                            val print = element.ofMap()?.print()
                            Timber.tag("TransformData").d("data=${print ?: "@null"}")
                            element
                        }

                        is JsonArray -> {
                            realData = realData.jsonArray
                            val type = requestedType.kotlinType ?: throw NoTypeException(requestedType.toString())
                            val element = json.decodeFromJsonElement(serializer(type), realData)
                            element
                        }

                        // is JsonPrimitive -> {
                        //     realData = realData.jsonPrimitive
                        //     if (realData.isString) {
                        //         return@transformResponseBody realData.content
                        //     }
                        //     Timber.tag("TransformData").d("primitive=$realData")
                        //     val type = requestedType.kotlinType ?: throw NoTypeException(requestedType.toString())
                        //     json.decodeFromJsonElement(serializer(type), realData)
                        // }

                        else -> null
                    }
                } catch (e: Exception) {
                    Timber.tag("TransformDataException").e(e)
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

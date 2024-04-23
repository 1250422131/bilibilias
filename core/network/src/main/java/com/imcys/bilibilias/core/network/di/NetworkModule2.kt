package com.imcys.bilibilias.core.network.di

import android.content.Context
import android.os.Build.VERSION.SDK_INT
import androidx.tracing.trace
import coil.ImageLoader
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import coil.decode.SvgDecoder
import coil.decode.VideoFrameDecoder
import coil.util.DebugLogger
import com.imcys.bilibilias.core.common.utils.ofMap
import com.imcys.bilibilias.core.common.utils.print
import com.imcys.bilibilias.core.datastore.LoginInfoDataSource
import com.imcys.bilibilias.core.model.Box
import com.imcys.bilibilias.core.model.Response
import com.imcys.bilibilias.core.network.BuildConfig
import com.imcys.bilibilias.core.network.Parameter
import com.imcys.bilibilias.core.network.api.BROWSER_USER_AGENT
import com.imcys.bilibilias.core.network.api.BiliBiliAsApi
import com.imcys.bilibilias.core.network.api.BilibiliApi
import com.imcys.bilibilias.core.network.ktor.AsCookiesStorage
import com.imcys.bilibilias.core.network.ktor.plugin.logging.JsonAwareLogLevel
import com.imcys.bilibilias.core.network.ktor.plugin.logging.JsonAwareLogger
import com.imcys.bilibilias.core.network.ktor.plugin.logging.JsonAwareLogging
import com.imcys.bilibilias.core.network.utils.WBIUtils
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import github.leavesczy.monitor.MonitorInterceptor
import io.github.aakira.napier.Napier
import io.ktor.client.HttpClient
import io.ktor.client.call.HttpClientCall
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.BrowserUserAgent
import io.ktor.client.plugins.HttpRequestRetry
import io.ktor.client.plugins.HttpSend
import io.ktor.client.plugins.Sender
import io.ktor.client.plugins.addDefaultResponseValidation
import io.ktor.client.plugins.api.ClientPlugin
import io.ktor.client.plugins.api.createClientPlugin
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.cookies.HttpCookies
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.plugin
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.parameter
import io.ktor.client.statement.request
import io.ktor.http.HttpHeaders
import io.ktor.http.ParametersBuilderImpl
import io.ktor.serialization.kotlinx.json.json
import io.ktor.util.AttributeKey
import io.ktor.utils.io.ByteReadChannel
import io.ktor.utils.io.jvm.javaio.toInputStream
import kotlinx.coroutines.flow.first
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromStream
import kotlinx.serialization.serializer
import okhttp3.Cache
import okhttp3.Dispatcher
import okhttp3.OkHttpClient
import okhttp3.brotli.BrotliInterceptor
import java.io.File
import java.util.concurrent.ExecutorService
import java.util.concurrent.TimeUnit
import javax.inject.Singleton
import kotlin.reflect.typeOf

data class WrapperClient(val client: HttpClient)

internal val requireWbi = AttributeKey<Boolean>("requireWbi")
internal val requireCSRF = AttributeKey<Boolean>("requireCSRF")

@Module
@InstallIn(SingletonComponent::class)
class NetworkModule2 {
    @Provides
    @Singleton
    fun imageLoader(
        okHttpClient: dagger.Lazy<OkHttpClient>,
        @ApplicationContext application: Context,
    ): ImageLoader = trace("AsImageLoader") {
        ImageLoader.Builder(application)
            .callFactory { okHttpClient.get() }
            .components {
                add(SvgDecoder.Factory())
                add(VideoFrameDecoder.Factory())
                if (SDK_INT >= 28) {
                    add(ImageDecoderDecoder.Factory())
                } else {
                    add(GifDecoder.Factory())
                }
            }
            .respectCacheHeaders(false)
            .apply {
                if (BuildConfig.DEBUG) {
                    logger(DebugLogger())
                }
            }
            .build()
    }

    @Provides
    @Singleton
    fun provideBaseOkhttpClient(
        executorService: ExecutorService,
        @ApplicationContext context: Context
    ): OkHttpClient =
        OkHttpClient.Builder()
            .addNetworkInterceptor { chain ->
                val request = chain.request()
                val requestWithUserAgent = request.newBuilder()
                    .removeHeader(HttpHeaders.UserAgent)
                    .header(HttpHeaders.UserAgent, BROWSER_USER_AGENT)
                    .build()
                chain.proceed(requestWithUserAgent)
            }
            .addInterceptor(BrotliInterceptor)
            .pingInterval(1, TimeUnit.SECONDS)
            .dispatcher(Dispatcher(executorService))
            .cache(Cache(File(context.cacheDir.path, "okhttp_cache"), 1024 * 1024 * 50))
            .addInterceptor(MonitorInterceptor())
            .build()

    @Singleton
    @Provides
    fun provideHttpClient(
        asLogger: JsonAwareLogger,
        json: Json,
        transform: ClientPlugin<Unit>,
        asCookiesStorage: AsCookiesStorage,
        loginInfoDataSource: LoginInfoDataSource,
        okHttpClient: OkHttpClient
    ): WrapperClient {
        val client = HttpClient(
            OkHttp.create { preconfigured = okHttpClient }
        ) {
            defaultRequest {
                url(BilibiliApi.API_BILIBILI)
            }
            BrowserUserAgent()
            addDefaultResponseValidation()
            JsonAwareLogging {
                logger = asLogger
                level = JsonAwareLogLevel.BODY
                filter {
                    it.url.host == BilibiliApi.API_HOST ||
                        it.url.host == BiliBiliAsApi.API_HOST
                }
            }
            install(HttpCookies) {
                storage = asCookiesStorage
            }
            install(transform)

            install(ContentNegotiation) {
                json(json)
            }

            install(HttpRequestRetry) {
                retryOnServerErrors(maxRetries = 5)
                exponentialDelay()
            }
        }
        client.plugin(HttpSend).intercept { request ->
            wbiIntercept(request, loginInfoDataSource)
        }
        return WrapperClient(client)
    }

    private suspend fun Sender.wbiIntercept(
        request: HttpRequestBuilder,
        loginInfoDataSource: LoginInfoDataSource,
    ): HttpClientCall {
        if (request.attributes.getOrNull(requireCSRF) == true) {
            val cookie = loginInfoDataSource.cookieStore.first()["bili_jct"]
            if (cookie != null) {
                request.parameter("csrf", cookie.value_)
            }
        }
        if (request.attributes.getOrNull(requireWbi) == true) {
            val params = request.url.parameters
            val signatureParams = mutableListOf<Parameter>()
            for ((k, v) in params.entries()) {
                signatureParams.add(Parameter(k, v.joinToString()))
            }
            val signature = WBIUtils.encWbi(signatureParams, loginInfoDataSource.mixKey.first())
            val newParameter = ParametersBuilderImpl()
            for ((n, v) in signature) {
                newParameter.append(n, v)
            }

            request.url.encodedParameters = newParameter
        }
        return execute(request)
    }

    @OptIn(ExperimentalSerializationApi::class)
    @Provides
    @Singleton
    fun provideTransformData(
        json: Json,
        loginInfoDataSource: LoginInfoDataSource
    ): ClientPlugin<Unit> = createClientPlugin("TransformData") {
        transformResponseBody { request, content, requestedType ->
            if (request.request.url.host == BiliBiliAsApi.API_HOST) return@transformResponseBody null
            if (requestedType.kotlinType == typeOf<ByteReadChannel>() ||
                requestedType.kotlinType == typeOf<Response>()
            ) {
                return@transformResponseBody null
            }

            val box = json.decodeFromStream(
                Box.serializer(serializer(requestedType.kotlinType!!)),
                content.toInputStream()
            )
            if (box.code == ACCOUNT_NOT_LOGGED_IN) {
                loginInfoDataSource.setLoginState(false)
            }
            if (box.code != SUCCESS) {
                throw ApiIOException(
                    box.code,
                    box.message +
                        "网络接口: ${request.request.url.encodedPath} 发生解析错误" +
                        "\n链接: ${request.request.url}",
                    box.data?.ofMap()?.print()
                )
            }
            box.data
        }
    }

    companion object {
        private const val SUCCESS = 0
        private const val ACCOUNT_NOT_LOGGED_IN = -101
    }
}

class ApiIOException(code: Int, errorMessage: String?, content: String?) :
    Exception(errorMessage)

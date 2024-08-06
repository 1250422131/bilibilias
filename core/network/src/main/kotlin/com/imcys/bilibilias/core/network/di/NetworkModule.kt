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
import com.imcys.bilibilias.core.datastore.UsersDataSource
import com.imcys.bilibilias.core.model.Box
import com.imcys.bilibilias.core.model.Response
import com.imcys.bilibilias.core.network.BuildConfig
import com.imcys.bilibilias.core.network.api.BROWSER_USER_AGENT
import com.imcys.bilibilias.core.network.api.BiliBiliAsApi
import com.imcys.bilibilias.core.network.api.BilibiliApi
import com.imcys.bilibilias.core.network.ktor.AsCookiesStorage
import com.imcys.bilibilias.core.network.ktor.plugin.logging.JsonAwareLogLevel
import com.imcys.bilibilias.core.network.ktor.plugin.logging.JsonAwareLogger
import com.imcys.bilibilias.core.network.ktor.plugin.logging.JsonAwareLogging
import com.imcys.bilibilias.core.network.ktor.wbiIntercept
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import github.leavesczy.monitor.MonitorInterceptor
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.HttpRequestRetry
import io.ktor.client.plugins.HttpSend
import io.ktor.client.plugins.UserAgent
import io.ktor.client.plugins.addDefaultResponseValidation
import io.ktor.client.plugins.api.ClientPlugin
import io.ktor.client.plugins.api.createClientPlugin
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.cookies.HttpCookies
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.plugin
import io.ktor.client.statement.request
import io.ktor.http.HttpHeaders
import io.ktor.serialization.kotlinx.json.json
import io.ktor.util.AttributeKey
import io.ktor.utils.io.ByteReadChannel
import io.ktor.utils.io.jvm.javaio.toInputStream
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.encodeToString
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

internal val requireWbi = AttributeKey<Boolean>("requireWbi")
internal val requireCSRF = AttributeKey<Boolean>("requireCSRF")

@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {
    @OptIn(ExperimentalSerializationApi::class)
    @Provides
    @Singleton
    fun provideJson(): Json = trace("AsJson") {
        Json {
            prettyPrint = true
            ignoreUnknownKeys = true
            // 使用默认值覆盖 null
            coerceInputValues = true
            prettyPrintIndent = "  "
        }
    }

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
        @ApplicationContext context: Context,
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
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .connectTimeout(30, TimeUnit.SECONDS)
            .build()

    @Singleton
    @Provides
    fun provideHttpClient(
        asLogger: JsonAwareLogger,
        json: Json,
        transform: ClientPlugin<Unit>,
        asCookiesStorage: AsCookiesStorage,
        okHttpClient: OkHttpClient,
    ): HttpClient {
        val client = HttpClient(
            OkHttp.create { preconfigured = okHttpClient },
        ) {
            defaultRequest {
                url(BilibiliApi.API_BILIBILI)
            }
            addDefaultResponseValidation()
            JsonAwareLogging {
                logger = asLogger
                level = JsonAwareLogLevel.ALL
                filter {
                    it.url.host == BilibiliApi.API_HOST || it.url.host == BiliBiliAsApi.API_HOST
                }
            }
            install(UserAgent) {
                agent = BROWSER_USER_AGENT
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
            wbiIntercept(request)
        }
        return client
    }

    @OptIn(ExperimentalSerializationApi::class)
    @Provides
    @Singleton
    fun provideTransformData(
        json: Json,
        usersDataSource: UsersDataSource,
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
                content.toInputStream(),
            )
            if (box.code == ACCOUNT_NOT_LOGGED_IN) {
                usersDataSource.setLoginState(false)
            }
            if (box.code != SUCCESS) {
                throw ApiIOException(
                    box.code,
                    box.message +
                        "网络接口: ${request.request.url.encodedPath} 发生解析错误 " +
                        "\n链接: ${request.request.url}",
                    json.encodeToString(box),
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

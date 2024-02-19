package com.imcys.network.di

import android.content.*
import coil.*
import coil.decode.*
import coil.disk.*
import coil.util.*
import com.imcys.bilibilias.okdownloader.*
import com.imcys.common.di.*
import com.imcys.common.logger.*
import com.imcys.common.utils.*
import com.imcys.datastore.fastkv.*
import com.imcys.model.*
import com.imcys.network.*
import com.imcys.network.configration.*
import com.imcys.network.constant.*
import com.imcys.network.repository.*
import com.imcys.network.utils.*
import com.squareup.wire.*
import dagger.*
import dagger.hilt.*
import dagger.hilt.android.qualifiers.*
import dagger.hilt.components.*
import github.leavesczy.monitor.*
import io.ktor.client.*
import io.ktor.client.engine.*
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.*
import io.ktor.client.plugins.api.*
import io.ktor.client.plugins.cache.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.cookies.*
import io.ktor.client.plugins.logging.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.serialization.kotlinx.protobuf.*
import io.ktor.util.*
import io.ktor.utils.io.*
import io.ktor.utils.io.jvm.javaio.*
import kotlinx.coroutines.*
import kotlinx.serialization.*
import kotlinx.serialization.json.*
import okhttp3.*
import okhttp3.Dispatcher
import okhttp3.EventListener
import okhttp3.brotli.*
import timber.log.*
import java.io.*
import java.util.concurrent.*
import javax.inject.*
import kotlin.reflect.*

@Qualifier
annotation class BaseOkhttpClient

@Qualifier
internal annotation class ProjectOkhttpClient

internal val requireWbi = AttributeKey<Boolean>("requireWbi")

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    @Provides
    @Singleton
    fun provideImageLoader(
        @BaseOkhttpClient okHttpClient: OkHttpClient,
        @ApplicationContext application: Context
    ): ImageLoader = ImageLoader.Builder(application)
        .okHttpClient { okHttpClient }
        .components {
            add(SvgDecoder.Factory())
            add(GifDecoder.Factory())
        }
        .diskCache {
            DiskCache.Builder()
                .directory(File(application.cacheDir, "coil_cache"))
                .build()
        }
        .respectCacheHeaders(false)
        .apply {
            if (BuildConfig.DEBUG) {
                logger(DebugLogger())
            }
        }
        .build()

    @Provides
    @Singleton
    @BaseOkhttpClient
    fun provideBaseOkhttpClient(
        executorService: ExecutorService,
        networkListenerFactory: EventListener.Factory
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
            .eventListenerFactory(networkListenerFactory)
            .addInterceptor(BrotliInterceptor)
            .pingInterval(1, TimeUnit.SECONDS)
            .dispatcher(Dispatcher(executorService))
            .build()

    @Provides
    @Singleton
    fun provideDownload(
        @BaseOkhttpClient okHttpClient: OkHttpClient,
        executorService: ExecutorService
    ): Downloader =
        Downloader.Builder()
            .okHttpClientFactory { okHttpClient }
            .downloadPool(DownloadPool(executorService))
            .build()

    @Provides
    @Singleton
    fun provideExecutorService(
        @com.imcys.common.di.Dispatcher(AsDispatchers.IO) ioDispatch: CoroutineDispatcher
    ): ExecutorService =
        ioDispatch.asExecutor().asNonTerminatingExecutorService()

    @Provides
    @Singleton
    fun provideGrpcClient(@BaseOkhttpClient okHttpClient: OkHttpClient): GrpcClient =
        GrpcClient.Builder()
            .client(
                okHttpClient.newBuilder()
                    .protocols(listOf(Protocol.H2_PRIOR_KNOWLEDGE))
                    .build()
            )
            .baseUrl(API_BILIBILI)
            .build()

    @Provides
    @Singleton
    @ProjectOkhttpClient
    fun provideProjectOkhttpClient(
        @ApplicationContext context: Context,
        @BaseOkhttpClient okHttpClient: OkHttpClient
    ): OkHttpClient = okHttpClient.newBuilder()
        .cache(Cache(File(context.cacheDir.path, "okhttp_cache"), 1024 * 1024 * 50))
        .addInterceptor(MonitorInterceptor())
        .build()

    @Provides
    @Singleton
    fun provideHttpClientEngine(@ProjectOkhttpClient okHttpClient: OkHttpClient): HttpClientEngine =
        OkHttp.create {
            preconfigured = okHttpClient
        }

    @OptIn(ExperimentalSerializationApi::class)
    @Provides
    @Singleton
    @Suppress("LongParameterList")
    fun provideHttpClient(
        httpClientEngine: HttpClientEngine,
        json: Json,
        transform: ClientPlugin<Unit>,
        persistentCache: PersistentCache,
        loggerManager: LoggerManager,
        wbiKeyStorage: WbiKeyStorage
    ): HttpClient {
        val client = HttpClient(httpClientEngine) {
            defaultRequest {
                url(API_BILIBILI)
            }
            BrowserUserAgent()
            addDefaultResponseValidation()
            Logging {
                logger = loggerManager
                level = LogLevel.NONE
            }
            install(HttpCookies) {
                storage = CookieManager
            }
            install(transform)

            install(ContentNegotiation) {
                json(json)
                protobuf()
            }

            install(HttpRequestRetry) {
                retryOnServerErrors(maxRetries = 5)
                exponentialDelay()
            }
            install(HttpCache) {
                publicStorage(persistentCache)
            }
        }
        client.plugin(HttpSend).intercept { request ->
            wbiIntercept(request, wbiKeyStorage)
        }
        return client
    }

    private suspend fun Sender.wbiIntercept(
        request: HttpRequestBuilder,
        wbiKeyStorage: WbiKeyStorage
    ) = if (request.attributes.getOrNull(requireWbi) == true) {
        val params = request.url.parameters
        val signatureParams = mutableListOf<Parameter>()
        for ((k, v) in params.entries()) {
            signatureParams.add(Parameter(k, v.joinToString()))
        }
        val signature = WBIUtils.encWbi(signatureParams, wbiKeyStorage.mixKey)
        val newParameter = ParametersBuilderImpl()
        for ((n, v) in signature) {
            newParameter.append(n, v)
        }

        request.url.encodedParameters = newParameter
        Timber.d("param=${newParameter.build()}, mixKey=${wbiKeyStorage.mixKey}")
        execute(request)
    } else {
        execute(request)
    }

    @OptIn(ExperimentalSerializationApi::class)
    @Provides
    @Singleton
    fun provideTransformData(json: Json): ClientPlugin<Unit> =
        createClientPlugin("TransformData") {
            transformResponseBody { request, content, requestedType ->
                if (requestedType.kotlinType == typeOf<ByteReadChannel>()) return@transformResponseBody null

                val box = json.decodeFromStream(
                    Box.serializer(serializer(requestedType.kotlinType!!)),
                    content.toInputStream()
                )
                if (box.code == ACCOUNT_NOT_LOGGED_IN) {
                    PersistentCookie.logging = false
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

    private const val SUCCESS = 0
    private const val ACCOUNT_NOT_LOGGED_IN =-101

    /**
     * 权限类 代码 含义 -1 应用程序不存在或已被封禁 -2 Access Key 错误 -3 API 校验密匙错误 -4 调用方对该
     * Method 没有权限 -101 账号未登录 -102 账号被封停 -103 积分不足 -104 硬币不足 -105 验证码错误 -106
     * 账号非正式会员或在适应期 -107 应用不存在或者被封禁 -108 未绑定手机 -110 未绑定手机 -111 csrf 校验失败 -112
     * 系统升级中 -113 账号尚未实名认证 -114 请先绑定手机 -115 请先完成实名认证
     */

    /**
     * 请求类 代码 含义 -304 木有改动 -307 撞车跳转 -400 请求错误 -401 未认证 (或非法请求) -403 访问权限不足
     * -404 啥都木有 -405 不支持该方法 -409 冲突 -412 请求被拦截 (客户端 ip 被服务端风控) -500 服务器错误 -503
     * 过载保护,服务暂不可用 -504 服务调用超时 -509 超出限制 -616 上传文件不存在 -617 上传文件太大 -625 登录失败次数太多
     * -626 用户不存在 -628 密码太弱 -629 用户名或密码错误 -632 操作对象数量限制 -643 被锁定 -650 用户等级太低
     * -652 重复的用户 -658 Token 过期 -662 密码时间戳过期 -688 地理区域限制 -689 版权限制 -701 扣节操失败
     * -799 请求过于频繁，请稍后再试 -8888 对不起，服务器开小差了~ (ಥ﹏ಥ)
     */
    internal class ApiIOException(code: Int, errorMessage: String?, content: String?) :
        Exception(errorMessage)
}

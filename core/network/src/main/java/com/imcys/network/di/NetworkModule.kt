package com.imcys.network.di

import android.content.Context
import com.imcys.common.utils.ofMap
import com.imcys.common.utils.print
import com.imcys.network.configration.CacheManager
import com.imcys.network.configration.CookieManager
import com.imcys.network.configration.LoggerManager
import com.imcys.network.constants.BROWSER_USER_AGENT
import com.imcys.network.constants.ROAM_API
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import github.leavesczy.monitor.MonitorInterceptor
import io.ktor.client.HttpClient
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.BrowserUserAgent
import io.ktor.client.plugins.HttpRequestRetry
import io.ktor.client.plugins.HttpSend
import io.ktor.client.plugins.api.ClientPlugin
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
import io.ktor.http.HttpHeaders
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
import okhttp3.OkHttpClient
import timber.log.Timber
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {

    @Provides
    @Singleton
    fun provideOkhttp(): OkHttpClient = OkHttpClient.Builder()
        .addInterceptor { chain ->
            chain.proceed(
                chain.request()
                    .newBuilder()
                    .addHeader(HttpHeaders.UserAgent, BROWSER_USER_AGENT)
                    .build()
            )
        }
        .build()

    @Provides
    @Singleton
    fun provideHttpClientEngine(okHttpClient: OkHttpClient, @ApplicationContext context: Context): HttpClientEngine = OkHttp.create {
        preconfigured = okHttpClient
        addInterceptor(MonitorInterceptor(context))
    }

    @OptIn(ExperimentalSerializationApi::class)
    @Provides
    @Singleton
    fun provideHttpClient(
        httpClientEngine: HttpClientEngine,
        json: Json,
        transform: ClientPlugin<Unit>
    ): HttpClient = HttpClient(httpClientEngine) {
        defaultRequest { url(ROAM_API) }
        BrowserUserAgent()
        ContentEncoding()
        Logging { logger = LoggerManager(); level = LogLevel.BODY }
        ResponseObserver { response ->
            Timber.tag("http client").d(response.toString())
        }
        install(HttpCookies) {
            storage = CookieManager()
        }
        install(transform)

        install(ContentNegotiation) {
            json(json)
            // io.ktor.serialization.kotlinx.protobuf.protobuf()
        }

        install(HttpRequestRetry) {
            retryOnServerErrors(maxRetries = 5)
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
            val originalCall = execute(request)
            if (originalCall.response.status.value !in 100..399) {
                execute(request)
            } else {
                originalCall
            }
        }
    }

    @Provides
    @Singleton
    fun provideTransformData(json: Json): ClientPlugin<Unit> = createClientPlugin("TransformData") {
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

    /**
     * 权限类
     * 代码	含义
     * -1	应用程序不存在或已被封禁
     * -2	Access Key 错误
     * -3	API 校验密匙错误
     * -4	调用方对该 Method 没有权限
     * -101	账号未登录
     * -102	账号被封停
     * -103	积分不足
     * -104	硬币不足
     * -105	验证码错误
     * -106	账号非正式会员或在适应期
     * -107	应用不存在或者被封禁
     * -108	未绑定手机
     * -110	未绑定手机
     * -111	csrf 校验失败
     * -112	系统升级中
     * -113	账号尚未实名认证
     * -114	请先绑定手机
     * -115	请先完成实名认证
     */

    /** 请求类
     * 代码	含义
     * -304	木有改动
     * -307	撞车跳转
     * -400	请求错误
     * -401	未认证 (或非法请求)
     * -403	访问权限不足
     * -404	啥都木有
     * -405	不支持该方法
     * -409	冲突
     * -412	请求被拦截 (客户端 ip 被服务端风控)
     * -500	服务器错误
     * -503	过载保护,服务暂不可用
     * -504	服务调用超时
     * -509	超出限制
     * -616	上传文件不存在
     * -617	上传文件太大
     * -625	登录失败次数太多
     * -626	用户不存在
     * -628	密码太弱
     * -629	用户名或密码错误
     * -632	操作对象数量限制
     * -643	被锁定
     * -650	用户等级太低
     * -652	重复的用户
     * -658	Token 过期
     * -662	密码时间戳过期
     * -688	地理区域限制
     * -689	版权限制
     * -701	扣节操失败
     * -799	请求过于频繁，请稍后再试
     * -8888 对不起，服务器开小差了~ (ಥ﹏ಥ)
     */
    internal class ApiIOException(errorMessage: String?) : IOException(errorMessage)
    internal class NullResponseDataIOException : Exception()
    internal class NoTypeException(msg: String) : Exception(msg)
}

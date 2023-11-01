package com.imcys.bilibilias.common.base.utils.http

import com.imcys.bilibilias.common.base.api.BiliBiliAsApi
import com.imcys.bilibilias.common.base.constant.USER_AGENT
import com.imcys.bilibilias.common.base.utils.file.SystemUtil
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.HttpRequestRetry
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.forms.submitForm
import io.ktor.client.request.get
import io.ktor.client.request.headers
import io.ktor.http.Parameters
import io.ktor.serialization.gson.gson
import kotlin.collections.set
@Deprecated("请使用 ktor")
object KtHttpUtils {

    val params = mutableMapOf<String, String>()
    val headers = mutableMapOf<String, String>()

    var setCookies = ""
    @Deprecated("使用依赖注入")
    val httpClient = HttpClient(OkHttp) {

        expectSuccess = true

        install(Logging)

        install(ContentNegotiation) { gson() }

        // 请求失败
        install(HttpRequestRetry) {
            retryOnServerErrors(maxRetries = 2)
            exponentialDelay()
        }
    }

    suspend inline fun <reified T> asyncGet(url: String): T {
        checkUrl(url)
        val mBean: T = httpClient.get(url) {
            headers {
                this@KtHttpUtils.headers.forEach {
                    this.append(it.key, it.value)
                }
            }
        }.body()
        // 清空
        headers.clear()

        return mBean
    }

    suspend inline fun <reified T> asyncPost(url: String): T {
        checkUrl(url)

        val response = httpClient.submitForm(
            url = url,
            formParameters = Parameters.build {
                this@KtHttpUtils.params.forEach {
                    this.append(it.key, it.value)
                }
            },
        ) {
            headers {
                this@KtHttpUtils.headers.forEach {
                    this.append(it.key, it.value)
                }
            }
        }
        // 清空
        headers.clear()
        params.clear()
        return response.body()
    }

    /**
     * 添加请求头
     * @param key String
     * @param value String
     * @return HttpUtils
     */
    fun addHeader(key: String, value: String): KtHttpUtils {
        headers[key] = value
        return this
    }

    fun checkUrl(url: String) {
        headers[USER_AGENT] = if (url in "misakamoe") {
            SystemUtil.getUserAgent() + " BILIBILIAS/${BiliBiliAsApi.version}"
        } else {
           ""
        }
    }
}

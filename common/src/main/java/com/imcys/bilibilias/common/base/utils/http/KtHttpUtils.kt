package com.imcys.bilibilias.common.base.utils.http

import com.imcys.bilibilias.common.base.api.BiliBiliAsApi
import com.imcys.bilibilias.common.base.utils.file.SystemUtil
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.okhttp.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.client.request.*
import io.ktor.client.request.forms.*
import io.ktor.serialization.gson.*


object KtHttpUtils {

    var params = mutableMapOf<String, String>()
    var headers = mutableMapOf<String, String>()

    val httpClient = HttpClient(OkHttp) {

        expectSuccess = true

        install(Logging)

        install(ContentNegotiation) {
            gson { }
        }

        //请求失败
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
        //清空
        headers.clear()


        return mBean
    }

    suspend inline fun <reified T> asyncPost(url: String): T {
        checkUrl(url)
        val mBean: T = httpClient.post(url) {
            headers {
                this@KtHttpUtils.headers.forEach {
                    this.append(it.key, it.value)
                }
            }
            formData {
                this@KtHttpUtils.params.forEach {
                    this.append(it.key, it.value)
                }
            }

        }.body()
        //清空
        headers.clear()
        params.clear()
        return mBean
    }

    /**
     * 添加post的form参数
     * @param key String
     * @param value String
     * @return HttpUtils
     */
    fun addParam(key: String, value: String): KtHttpUtils {
        params[key] = value
        return this
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
        headers["user-agent"] = if (url in "misakamoe") {
            SystemUtil.getUserAgent() + " BILIBILIAS/${BiliBiliAsApi.version}"
        } else {
            "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/108.0.0.0 Safari/537.36 Edg/108.0.1462.54"
        }
    }

}
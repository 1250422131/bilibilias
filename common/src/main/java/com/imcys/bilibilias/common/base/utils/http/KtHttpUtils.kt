package com.imcys.bilibilias.common.base.utils.http

import com.imcys.bilibilias.common.base.api.BiliBiliAsApi
import com.imcys.bilibilias.common.base.constant.BROWSER_USER_AGENT
import com.imcys.bilibilias.common.base.constant.USER_AGENT
import com.imcys.bilibilias.core.model.IPostBody
import com.imcys.bilibilias.common.base.utils.file.SystemUtil
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.delete
import io.ktor.client.request.forms.submitForm
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpMessageBuilder
import io.ktor.http.Parameters
import io.ktor.http.contentType
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.collections.set

@Singleton
class KtHttpUtils @Inject constructor(val httpClient: HttpClient) {
    val params = mutableMapOf<String, Any>()
    val headers = mutableMapOf<String, String>()

    var setCookies = ""

    suspend inline fun <reified T> asyncGet(
        url: String,
        builder: HttpMessageBuilder.() -> Unit = {}
    ): T {
        checkUrl(url)
        val mBean: T = httpClient.get(url) {
            builder()
            this@KtHttpUtils.headers.forEach {
                header(it.key, it.value)
            }
        }.body()
        // 清空
        headers.clear()

        return mBean
    }

     inline fun < reified T> asyncGet(
    ): String {



        return T::class.java.simpleName
    }

    suspend inline fun <reified T> asyncPost(url: String): T {
        checkUrl(url)

        val response = httpClient.submitForm(
            url = url,
            formParameters = Parameters.build {
                this@KtHttpUtils.params.forEach {
                    this.append(it.key, it.value.toString())
                }
            },
        ) {
            this@KtHttpUtils.headers.forEach {
                header(it.key, it.value)
            }
        }
        // 清空
        headers.clear()
        params.clear()
        return response.body()
    }

    suspend inline fun <reified T> asyncPostJson(
        url: String,
        bodyObject: IPostBody,
    ): T {
        checkUrl(url)
        val response = httpClient.post(url) {
            contentType(ContentType.Application.Json)

            setBody(bodyObject)
            this@KtHttpUtils.headers.forEach {
                header(it.key, it.value)
            }
        }

        // 清空
        headers.clear()
        // 设置cookie
        // 获取所有 Set-Cookie 头部
        response.headers.getAll(HttpHeaders.SetCookie)?.forEach {
            setCookies += it
        }

        return response.body()
    }

    suspend inline fun <reified T> asyncDeleteJson(
        url: String,
        bodyObject: IPostBody,
    ): T {
        checkUrl(url)
        val response = httpClient.delete(url) {
            contentType(ContentType.Application.Json)

            setBody(bodyObject)

            this@KtHttpUtils.headers.forEach {
                header(it.key, it.value)
            }
        }

        // 清空
        headers.clear()

        return response.body()
    }

    /**
     * 添加post的form参数
     * @param key String
     * @param value String
     * @return HttpUtils
     */
    fun addParam(key: String, value: Any): KtHttpUtils {
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
        headers[USER_AGENT] = if (url in "misakamoe") {
            SystemUtil.getUserAgent() + " BILIBILIAS/${BiliBiliAsApi.version}"
        } else {
            BROWSER_USER_AGENT
        }
    }
}

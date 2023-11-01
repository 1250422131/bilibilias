package com.imcys.bilibilias.common.base.utils.http

import com.google.gson.Gson
import com.imcys.bilibilias.common.base.api.BiliBiliAsApi
import com.imcys.bilibilias.common.base.constant.USER_AGENT
import com.imcys.bilibilias.common.base.utils.file.SystemUtil
import kotlinx.coroutines.*
import okhttp3.*
import java.io.*

/**
 * @author imcys
 *
 * 此类为okhttp3的封装类
 */
@Deprecated("请使用 ktor")
open class HttpUtils {

    companion object {

        private val okHttpClient = OkHttpClient()

        private val params = mutableMapOf<String, String>()
        private val headers = mutableMapOf<String, String>()

        private val misakaMoeUa by lazy {
            SystemUtil.getUserAgent()
        }

        /**
         * 使用 OkHttp 库发送 GET 请求的方法
         * @param url String 请求地址
         * @param callBack Callback 请求完成后的回调函数
         */
        @JvmStatic
        fun get(url: String, callBack: Callback) {
            // 检验url，添加对应的ua
            checkUrl(url)
            // 创建请求对象
            val request: Request = Request.Builder().apply {
                // 将已设置的头信息添加到请求中
                headers.forEach {
                    addHeader(it.key, it.value)
                }
                // 设置请求地址
                url(url)
                // 设置为 GET 请求
                get()
            }.build()
            // 使用 OkHttp 的 enqueue 方法异步发送请求
            okHttpClient.newCall(request).enqueue(callBack)
        }

        /**
         * 使用 OkHttp 库发送 GET 请求的方法，并将响应数据自动映射为指定类型的对象
         * @param url String 请求地址
         * @param clz Class<T> 响应数据需要映射成的类的类型
         * @param method (data: T) -> Unit 请求完成后的回调函数，用于接收请求响应的结果
         */
        @JvmStatic
        fun <T> get(url: String, clz: Class<T>, method: (data: T) -> Unit) {
            // 检验url，添加对应的ua
            checkUrl(url)
            // 创建请求对象
            val request: Request = Request.Builder().apply {
                // 将已设置的头信息添加到请求中
                headers.forEach {
                    addHeader(it.key, it.value)
                }
                // 设置请求地址
                url(url)
                // 设置为 GET 请求
                get()
            }.build()
            // 使用 OkHttp 的 enqueue 方法异步发送请求
            okHttpClient.newCall(request).execute()
        }

        /**
         * 使用 OkHttp 库发送 GET 请求的方法
         * @param url String 请求地址
         * @param callBack Callback 请求完成后的回调函数
         */
        @JvmStatic
        suspend fun asyncGet(url: String): Deferred<Response> {
            // 检验url，添加对应的ua
            checkUrl(url)
            // 创建请求对象
            val request: Request = Request.Builder().apply {
                // 将已设置的头信息添加到请求中
                headers.forEach {
                    addHeader(it.key, it.value)
                }
                // 设置请求地址
                url(url)
                // 设置为 GET 请求
                get()
            }.build()
            // 使用 OkHttp 的 enqueue 方法异步发送请求
            return CoroutineScope(Dispatchers.Default).async {
                okHttpClient.newCall(request).execute()
            }
        }

        /**
         * 携程get请求
         * @param url String
         * @param clz Class<T>
         * @return T
         */
        @JvmStatic
        suspend fun <T : Any> asyncGet(url: String, clz: Class<T>): T {
            // 检验url，添加对应的ua
            checkUrl(url)
            // 创建请求对象
            val request: Request = Request.Builder().apply {
                // 将已设置的头信息添加到请求中
                headers.forEach {
                    addHeader(it.key, it.value)
                }
                // 设置请求地址
                url(url)
                // 设置为 GET 请求
                get()
            }.build()
            // 使用 OkHttp 的 enqueue 方法异步发送请求
            val response = okHttpClient.newCall(request).execute()

            return getJsonObject(response, clz)
        }

        private suspend fun <T> getJsonObject(response: Response, clz: Class<T>): T {
            return withContext(Dispatchers.IO) {
                Gson().fromJson(response.body?.string() ?: "empty string", clz)
            }
        }

        /**
         * post请求类
         * @param url String 请求地址
         * @param callBack Callback
         */
        @JvmStatic
        fun post(url: String, callBack: Callback) {
            // 检验url，添加对应的ua
            checkUrl(url)
            // 构建FormBody
            val formBody: FormBody.Builder = FormBody.Builder()
            // 添加params参数
            params.forEach {
                formBody.add(it.key, it.value)
            }
            // 构建request并且添加headers
            val request: Request = Request.Builder()
                .apply {
                    // 设置请求头
                    headers.forEach {
                        addHeader(it.key, it.value)
                    }
                    // 设置请求地址和参数
                    url(url)
                    post(formBody.build())
                }.build()
            okHttpClient.newCall(request).enqueue(callBack)
        }

        /**
         * 添加post的form参数
         * @param key String
         * @param value String
         * @return HttpUtils
         */
        @JvmStatic
        fun addParam(key: String, value: String): Companion {
            params[key] = value
            return this
        }

        /**
         * 添加请求头
         * @param key String
         * @param value String
         * @return HttpUtils
         */
        @JvmStatic
        fun addHeader(key: String, value: String): Companion {
            headers[key] = value
            return this
        }

        private fun checkUrl(url: String) {
            headers[USER_AGENT] = if (url.contains("misakamoe.com")) {
                misakaMoeUa + " BILIBILIAS/${BiliBiliAsApi.version}"
            } else {
              "BROWSER_USER_AGENT"
            }
        }
    }
}

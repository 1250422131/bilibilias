package com.imcys.bilibilias.common.base.utils.http

import com.google.gson.Gson
import com.imcys.bilibilias.common.base.api.BiliBiliAsApi
import com.imcys.bilibilias.common.base.app.BaseApplication
import com.imcys.bilibilias.common.base.extend.awaitResponse
import com.imcys.bilibilias.common.base.utils.file.SystemUtil
import kotlinx.coroutines.*
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.*
import java.net.HttpURLConnection
import java.net.URL


/**
 * @author imcys
 *
 * 此类为okhttp3的封装类
 */


open class HttpUtils {


    companion object {

        private val okHttpClient = OkHttpClient()

        private var params = mutableMapOf<String, String>()
        private var headers = mutableMapOf<String, String>()

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
            //检验url，添加对应的ua
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
            //检验url，添加对应的ua
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
            okHttpClient.newCall(request).enqueue(HttpCallback {
                // 使用 Gson 将响应数据映射为指定类型的对象
                val data = Gson().fromJson(it, clz)
                // 调用回调函数返回结果
                data?.let {
                    BaseApplication.handler.post {
                        method(it)
                    }
                }
            })
        }


        /**
         * 使用 OkHttp 库发送 GET 请求的方法
         * @param url String 请求地址
         * @param callBack Callback 请求完成后的回调函数
         */
        @JvmStatic
        suspend fun asyncGet(url: String): Deferred<Response> {
            //检验url，添加对应的ua
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
                okHttpClient.newCall(request).awaitResponse()
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
            //检验url，添加对应的ua
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
            val response = okHttpClient.newCall(request).awaitResponse()

            return getJsonObject(response, clz)

        }

        /**
         * 携程post请求
         * @param url String
         * @param clz Class<T>
         * @return T
         */
        @JvmStatic
        suspend fun <T : Any> asyncPost(url: String, clz: Class<T>): T {
            //检验url，添加对应的ua
            checkUrl(url)
            // 构建表单请求体
            val formBody: FormBody.Builder = FormBody.Builder()
            // 添加参数
            params.forEach {
                formBody.add(it.key, it.value)
            }
            // 构建请求并添加头信息
            val request: Request = Request.Builder()
                .apply {
                    // 设置请求头
                    headers.forEach {
                        addHeader(it.key, it.value)
                    }
                    // 设置请求地址和请求体
                    url(url)
                    post(formBody.build())
                }.build()
            // 使用 OkHttp 的 enqueue 方法异步发送请求
            val response = okHttpClient.newCall(request).awaitResponse()
            return getJsonObject(response, clz)

        }

        private suspend fun <T> getJsonObject(response: Response, clz: Class<T>): T {
            return withContext(Dispatchers.IO) {
                Gson().fromJson(response.body?.string() ?: "empty string", clz)

            }
        }


        /**
         * 使用 OkHttp 库发送 POST 请求的方法，并将响应数据自动映射为指定类型的对象
         * @param url String 请求地址
         * @param clz Class<T> 响应数据需要映射成的类的类型
         * @param responseResult (data: T) -> Unit 请求完成后的回调函数，用于接收请求响应的结果
         */
        @JvmStatic
        fun <T> post(url: String, clz: Class<T>, responseResult: (data: T) -> Unit) {
            //检验url，添加对应的ua
            checkUrl(url)
            // 构建表单请求体
            val formBody: FormBody.Builder = FormBody.Builder()
            // 添加参数
            params.forEach {
                formBody.add(it.key, it.value)
            }
            // 构建请求并添加头信息
            val request: Request = Request.Builder()
                .apply {
                    // 设置请求头
                    headers.forEach {
                        addHeader(it.key, it.value)
                    }
                    // 设置请求地址和请求体
                    url(url)
                    post(formBody.build())
                }.build()
            // 使用 OkHttp 的 enqueue 方法异步发送请求
            okHttpClient.newCall(request).enqueue(HttpCallback {
                // 使用 Gson 将响应数据映射为指定类型的对象
                val data = Gson().fromJson(it, clz)
                // 调用回调函数返回结果
                data.let {
                    BaseApplication.handler.post {
                        responseResult(it)
                    }
                }
            })

        }

        /**
         * post请求类
         * @param url String 请求地址
         * @param callBack Callback
         */
        @JvmStatic
        fun post(url: String, callBack: Callback) {
            //检验url，添加对应的ua
            checkUrl(url)
            //构建FormBody
            val formBody: FormBody.Builder = FormBody.Builder()
            //添加params参数
            params.forEach {
                formBody.add(it.key, it.value)
            }
            //构建request并且添加headers
            val request: Request = Request.Builder()
                .apply {
                    //设置请求头
                    headers.forEach {
                        addHeader(it.key, it.value)
                    }
                    //设置请求地址和参数
                    url(url)
                    post(formBody.build())
                }.build()
            okHttpClient.newCall(request).enqueue(callBack)

        }


        /**
         * post提交Json
         * @param url String 请求地址
         * @param jsonString String 请求json
         * @param callBack Callback
         */
        @JvmStatic
        fun postJson(url: String, jsonString: String, callBack: Callback) {
            //检验url，添加对应的ua
            checkUrl(url)
            val stringBody =
                jsonString.toRequestBody("application/json;charset=utf-8".toMediaType())
            val request: Request = Request.Builder().apply {
                headers.forEach {
                    addHeader(it.key, it.value)
                }
                //设置请求地址和参数
                url(url)
                post(stringBody)
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

        /**
         * 向指定 URL 发送POST方法的请求
         *
         * @param url   发送请求的 URL
         * @param param 请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
         * @return 所代表远程资源的响应结果
         * @throws Exception
         */
        fun doCardPost(url: String?, param: String?, Cookie: String?): String? {
            var out: PrintWriter? = null
            var `in`: BufferedReader? = null
            var result: String? = ""
            try {
                val realUrl = URL(url)
                // 打开和URL之间的连接
                val conn: HttpURLConnection = realUrl
                    .openConnection() as HttpURLConnection
                // 设置通用的请求属性
                conn.setRequestProperty("accept", "*/*")
                conn.setRequestProperty("connection", "Keep-Alive")
                conn.requestMethod = "POST"
                conn.setRequestProperty("referer", " https://www.bilibili.com/")
                conn.setRequestProperty(
                    "User-Agent",
                    "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36"
                )
                conn.setRequestProperty("cookie", Cookie)
                conn.setRequestProperty("charset", "utf-8")
                conn.useCaches = false
                // 发送POST请求必须设置如下两行
                conn.doOutput = true
                conn.doInput = true
                conn.readTimeout = 5000
                conn.connectTimeout = 5000
                val sessionId = ""
                val cookieVal = ""
                val key: String? = null
                if (param != null && param.trim { it <= ' ' } != "") {
                    // 获取URLConnection对象对应的输出流
                    out = PrintWriter(conn.outputStream)
                    // 发送请求参数
                    out.print(param)
                    // flush输出流的缓冲
                    out.flush()
                }

                // 定义BufferedReader输入流来读取URL的响应
                `in` = BufferedReader(
                    InputStreamReader(conn.inputStream)
                )
                var line: String?
                while (`in`.readLine().also { line = it } != null) {
                    result += line
                }
            } catch (e: Exception) {
                e.printStackTrace()
            } // 使用finally块来关闭输出流、输入流
            finally {
                try {
                    out?.close()
                    `in`?.close()
                } catch (ex: IOException) {
                    ex.printStackTrace()
                }
            }
            return result
        }

        private fun checkUrl(url: String) {
            headers["user-agent"] = if (url.contains("misakamoe.com")) {
                misakaMoeUa + " BILIBILIAS/${BiliBiliAsApi.version}"
            } else {
                "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/108.0.0.0 Safari/537.36 Edg/108.0.1462.54"
            }
        }

    }


}
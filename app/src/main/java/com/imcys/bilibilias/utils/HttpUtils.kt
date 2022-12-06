package com.imcys.bilibilias.utils

import com.google.gson.Gson
import com.imcys.bilibilias.base.app.App
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import org.xutils.common.task.PriorityExecutor
import org.xutils.http.RequestParams
import org.xutils.x
import java.io.File


/**
 * @author imcys
 *
 * 此类为okhttp3的封装类
 */


class HttpUtils {


    companion object {

        private val okHttpClient = OkHttpClient()


        private val TAG = javaClass.simpleName
        private var params = mutableMapOf<String, String>()
        private var headers = mutableMapOf<String, String>()


        /**
         * 下载文件方法
         * @param url String
         * @param callBack ProgressCallback<File>
         */
        @JvmStatic
        fun downLoadFile(url: String, callBack: org.xutils.common.Callback.ProgressCallback<File>) {
            val requestParams = RequestParams(url).apply {
                this@Companion.headers.forEach {
                    addHeader(it.key, it.value)
                }

                saveFilePath = ""
                executor = PriorityExecutor(1, true)
                //自定义线程池,有效的值范围[1, 3], 设置为3时, 可能阻塞图片加载.
                isCancelFast = true //是否可以被立即停止.

            }
            x.http().get(requestParams, callBack)
        }

        /**
         * 使用 OkHttp 库发送 GET 请求的方法
         * @param url String 请求地址
         * @param callBack Callback 请求完成后的回调函数
         */
        @JvmStatic
        fun get(url: String, callBack: Callback) {

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
                    App.handler.post {
                        method(it)
                    }
                }
            })
        }


        /**
         * 使用 OkHttp 库发送 POST 请求的方法，并将响应数据自动映射为指定类型的对象
         * @param url String 请求地址
         * @param clz Class<T> 响应数据需要映射成的类的类型
         * @param responseResult (data: T) -> Unit 请求完成后的回调函数，用于接收请求响应的结果
         */
        @JvmStatic
        fun <T> post(url: String, clz: Class<T>, responseResult: (data: T) -> Unit) {
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
                    App.handler.post {
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
    }


}
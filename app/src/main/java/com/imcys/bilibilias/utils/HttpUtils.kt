package com.imcys.bilibilias.utils

import android.annotation.SuppressLint
import android.app.Application
import android.os.Build
import android.os.Handler
import androidx.annotation.RequiresApi
import com.google.gson.Gson
import com.imcys.bilibilias.base.app.App
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody


/**
 * @author imcys
 *
 * 此类为okhttp3的封装类
 */


class HttpUtils {


    companion object{

        val okHttpClient = OkHttpClient()


        private val TAG = HttpUtils::class.java.simpleName
        private var params = mutableMapOf<String, String>()
        private var headers = mutableMapOf<String, String>()


        /**
         * get请求执行方法
         * @param url String 请求地址
         * @param callBack Callback
         */
        @JvmStatic
        fun get(url: String, callBack: Callback) {

            val request: Request = Request.Builder().apply {
                headers.forEach {
                    addHeader(it.key, it.value)
                }
                url(url)
                get()
            }.build()
            okHttpClient.newCall(request).enqueue(callBack)
        }


        @JvmStatic
        fun <T> get(url: String, clz: Class<T>, method: (data: T) -> Unit) {
            val request: Request = Request.Builder().apply {
                headers.forEach {
                    addHeader(it.key, it.value)
                }
                url(url)
                get()
            }.build()
            okHttpClient.newCall(request).enqueue(HttpCallback {
                val data = Gson().fromJson(it, clz)
                data?.let {
                    App.handler.post {
                        method(it)
                    }
                }
            })
        }


        @JvmStatic
        fun <T> post(url: String, clz: Class<T>, responseResult: (data: T) -> Unit) {
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
            okHttpClient.newCall(request).enqueue(HttpCallback {
                val data = Gson().fromJson(it, clz)
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
            val stringBody = jsonString.toRequestBody("application/json;charset=utf-8".toMediaType())
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
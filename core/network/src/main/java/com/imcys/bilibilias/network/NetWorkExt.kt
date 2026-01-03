package com.imcys.bilibilias.network

import com.imcys.bilibilias.common.event.sendLoginErrorEvent
import com.imcys.bilibilias.common.event.sendRequestFrequentEvent
import com.imcys.bilibilias.network.config.APP_KEY
import com.imcys.bilibilias.network.model.BiliApiResponse
import com.imcys.bilibilias.network.utils.BiliAppSigner
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.client.statement.request
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext

fun <T> emptyNetWorkResult(data: T? = null) = NetWorkResult.Default<T>(data, null)

sealed class NetWorkResult<out T>(
    open val status: ApiStatus,
    open val data: T?,
    open val responseData: BiliApiResponse<T?>?,
    open val errorMsg: String?
) {
    data class Default<out T>(
        override val data: T?,
        override val responseData: BiliApiResponse<T?>?
    ) : NetWorkResult<T>(
        status = ApiStatus.DEFAULT,
        data = data,
        responseData = responseData,
        errorMsg = null
    )

    data class Success<out T>(
        override val data: T?,
        override val responseData: BiliApiResponse<T?>?
    ) : NetWorkResult<T>(
        status = ApiStatus.SUCCESS,
        data = data,
        responseData = responseData,
        errorMsg = null
    )

    data class Error<out T>(
        override val data: T?,
        override val responseData: BiliApiResponse<T?>?,
        val exception: String
    ) : NetWorkResult<T>(
        status = ApiStatus.ERROR,
        data = data,
        responseData = responseData,
        errorMsg = exception
    )

    data class Loading<out T>(
        val isLoading: Boolean
    ) : NetWorkResult<T>(
        status = ApiStatus.LOADING,
        data = null,
        responseData = null,
        errorMsg = null
    )
}


enum class ApiStatus {
    SUCCESS,
    ERROR,
    LOADING,
    DEFAULT,
}

typealias FlowNetWorkResult<Data> = Flow<NetWorkResult<Data?>>

suspend inline fun <reified Data> HttpClient.httpRequest(
    crossinline request: suspend HttpClient.() -> HttpResponse
): FlowNetWorkResult<Data?> =
    withContext(Dispatchers.IO) {
        flow {
            runCatching {
                emit(NetWorkResult.Loading(true))
                val response = request(this@httpRequest)
                val body = runCatching {
                    response.body<BiliApiResponse<Data?>>().apply {
                        // 请求参数补充
                        responseHeader = response.headers.entries()
                    }
                }.getOrNull()
                if (body != null) {
                    // 如果成功解析为 BiliApiResponse，使用原有逻辑
                    Pair(response, body)
                } else {
                    val directData = response.body<Data?>()
                    val mockResponse = BiliApiResponse<Data?>(
                        code = 0,
                        data = directData,
                        result = null,
                        message = null,
                        ttl = 0,
                        responseHeader = response.headers.entries()
                    )
                    Pair(response, mockResponse)
                }
            }.onSuccess {
                val body = it.second
                val response = it.first
                // 异形JSON
                val mData = body.data ?: body.result
                when (body.code) {
                    0 -> {
                        emit(NetWorkResult.Success(mData, body))
                    }

                    -101 -> {
                        sendLoginErrorEvent()
                        emit(NetWorkResult.Error(mData, body, exception = body.message ?: ""))
                    }

                    -509 -> {
                        sendRequestFrequentEvent(url = response.request.url.toString())
                        emit(
                            NetWorkResult.Error(
                                mData,
                                body,
                                exception = body.message ?: "请求过于频繁，请稍后再试"
                            )
                        )
                    }

                    else -> {
                        emit(NetWorkResult.Error(mData, body, exception = body.message ?: ""))
                    }
                }
            }.onFailure {
                emit(NetWorkResult.Error(null, null, it.message ?: ""))
            }
        }
    }

inline fun <T, R> NetWorkResult<T>.mapData(transform: (T?, BiliApiResponse<T?>?) -> R?): NetWorkResult<R> {
    return when (this) {
        is NetWorkResult.Success -> {
            val transformedData = transform(data, responseData)
            val newResponse = this.responseData?.run { ->
                BiliApiResponse<R>(
                    code = code,
                    message = message,
                    data = transformedData,
                    result = transformedData,
                    ttl = ttl,
                    responseHeader = responseHeader
                )
            }
            NetWorkResult.Success<R>(transformedData, newResponse)
        }

        is NetWorkResult.Error -> NetWorkResult.Error(null, null, this.exception)
        is NetWorkResult.Loading -> NetWorkResult.Loading(this.isLoading)
        is NetWorkResult.Default -> {
            val transformedData = transform(data, responseData)
            NetWorkResult.Default(transformedData, null)
        }
    }
}

fun HttpRequestBuilder.parameterAppKey(): Unit =
    url.parameters.append(APP_KEY, BiliAppSigner.APP_KEY)

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
import io.ktor.client.statement.request
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

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

inline fun <reified Data> HttpClient.httpRequest(
    crossinline request: suspend HttpClient.() -> HttpResponse
): Flow<NetWorkResult<Data?>> =
    flow {
        emit(NetWorkResult.Loading(true))
        try {
            val response = request(this@httpRequest)
            val body = response.body<BiliApiResponse<Data?>>().apply {
                // 请求参数补充
                responseHeader = response.headers.entries()
            }
            val (data, apiResponse) = (body.data ?: body.result) to body
            handleSuccess(data, apiResponse, response)
        } catch (e: Exception) {
            emit(NetWorkResult.Error(null, null, e.message ?: ""))
        }
    }.flowOn(Dispatchers.IO)

@PublishedApi
internal suspend fun <Data> FlowCollector<NetWorkResult<Data?>>.handleSuccess(
    data: Data?,
    apiResponse: BiliApiResponse<Data?>,
    response: HttpResponse
) {
    when (apiResponse.code) {
        0 -> emit(NetWorkResult.Success(data, apiResponse))
        -101 -> {
            sendLoginErrorEvent()
            emit(NetWorkResult.Error(data, apiResponse, apiResponse.message ?: ""))
        }
        -509 -> {
            sendRequestFrequentEvent(url = response.request.url.toString())
            emit(NetWorkResult.Error(data, apiResponse, apiResponse.message ?: "请求过于频繁，请稍后再试"))
        }
        else -> emit(NetWorkResult.Error(data, apiResponse, apiResponse.message ?: ""))
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

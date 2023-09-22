package com.imcys.bilibilias.common.base.extend

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.ResponseException
import io.ktor.client.plugins.ServerResponseException
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.get
import io.ktor.client.request.request
import io.ktor.utils.io.errors.IOException
import kotlinx.serialization.SerializationException
import timber.log.Timber
import kotlin.Result

internal suspend inline fun <reified T, reified E> HttpClient.safeRequest(
    block: HttpRequestBuilder.() -> Unit,
): ApiResponse<T, E> =
    try {
        val response = request(block)
        ApiResponse.Success(response.body())
    } catch (e: ClientRequestException) {
        ApiResponse.Error.HttpError(e.response.status.value, e.errorBody())
    } catch (e: ServerResponseException) {
        ApiResponse.Error.HttpError(e.response.status.value, e.errorBody())
    } catch (e: IOException) {
        ApiResponse.Error.NetworkError
    } catch (e: SerializationException) {
        ApiResponse.Error.SerializationError
    }

internal suspend inline fun <reified T> HttpClient.safeGet(
    url: String,
    block: HttpRequestBuilder.() -> Unit = {},
): Result<T> =
    try {
        val response = get(url, block)
        Result.success(response.body())
    } catch (e: ClientRequestException) {
        Timber.tag("SafeGetCatching").d(e, "客户端异常")
        Result.failure(e)
    } catch (e: ServerResponseException) {
        Timber.tag("SafeGetCatching").d(e, "服务器异常")
        Result.failure(e)
    } catch (e: IOException) {
        Timber.tag("SafeGetCatching").d(e)
        Result.failure(e)
    } catch (e: SerializationException) {
        Timber.tag("SafeGetCatching").d(e, "序列化失败")
        Result.failure(e)
    }

internal suspend inline fun <reified E> ResponseException.errorBody(): E? =
    try {
        response.body()
    } catch (e: SerializationException) {
        null
    }

sealed class ApiResponse<out T, out E> {
    /**
     * Represents successful network responses (2xx).
     */
    data class Success<T>(val body: T) : ApiResponse<T, Nothing>()

    sealed class Error<E> : ApiResponse<Nothing, E>() {
        /**
         * Represents server (50x) and client (40x) errors.
         */
        data class HttpError<E>(val code: Int, val errorBody: E?) : Error<E>()

        /**
         * Represent IOExceptions and connectivity issues.
         */
        data object NetworkError : Error<Nothing>()

        /**
         * Represent SerializationExceptions.
         */
        data object SerializationError : Error<Nothing>()
    }
}

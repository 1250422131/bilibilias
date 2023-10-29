package com.imcys.bilibilias.common.base.extend

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.ServerResponseException
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText
import io.ktor.utils.io.errors.IOException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.serialization.SerializationException
import timber.log.Timber

internal suspend inline fun <reified T> HttpClient.safeGet(
    url: String,
    block: HttpRequestBuilder.() -> Unit = {},
): Result<T> = httpBlock { get(url, block).body() }

internal suspend inline fun <reified T> HttpClient.flowGet(
    url: String,
    crossinline block: HttpRequestBuilder.() -> Unit = {},
): Flow<Result<T>> = flow<T> { emit(get(url, block).body()) }.asResult()

internal suspend inline fun HttpClient.safeGetText(
    url: String,
    block: HttpRequestBuilder.() -> Unit = {},
): Result<String> = httpBlock { get(url, block).bodyAsText() }

private inline fun <T> httpBlock(block: () -> T) = try {
    Result.Success(block())
} catch (e: ClientRequestException) {
    Timber.d(e, "客户端异常")
    Result.Error(e)
} catch (e: ServerResponseException) {
    Timber.d(e, "服务器异常")
    Result.Error(e)
} catch (e: IOException) {
    Timber.d(e)
    Result.Error(e)
} catch (e: SerializationException) {
    Timber.d(e, "序列化失败")
    Result.Error(e)
} catch (e: Exception) {
    Timber.d(e)
    Result.Error(e)
}

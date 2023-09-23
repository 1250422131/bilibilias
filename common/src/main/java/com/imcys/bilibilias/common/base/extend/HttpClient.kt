package com.imcys.bilibilias.common.base.extend

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.ServerResponseException
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText
import io.ktor.utils.io.errors.IOException
import kotlinx.serialization.SerializationException
import timber.log.Timber
import kotlin.Result

internal suspend inline fun <reified T> HttpClient.safeGet(
    url: String,
    block: HttpRequestBuilder.() -> Unit = {},
): Result<T> =
    httpBlock {
        get(url, block).body()
    }

internal suspend inline fun HttpClient.safeGetText(
    url: String,
    block: HttpRequestBuilder.() -> Unit = {},
): Result<String> = httpBlock { get(url, block).bodyAsText() }

private inline fun <T> httpBlock(block: () -> T) = try {
    Result.success(block())
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

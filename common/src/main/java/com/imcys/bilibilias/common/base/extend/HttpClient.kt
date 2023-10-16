package com.imcys.bilibilias.common.base.extend

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

internal suspend inline fun <reified T> HttpClient.safeGet(
    url: String,
    crossinline block: HttpRequestBuilder.() -> Unit = {},
): Flow<Result<T>> =
    httpBlock { get(url, block).body() }

internal suspend fun HttpClient.safeGetText(
    url: String,
    block: HttpRequestBuilder.() -> Unit = {},
): Flow<Result<String>> = httpBlock { get(url, block).bodyAsText() }

private fun <T> httpBlock(block: suspend () -> T): Flow<Result<T>> =
    flow { emit(block()) }.asResult()

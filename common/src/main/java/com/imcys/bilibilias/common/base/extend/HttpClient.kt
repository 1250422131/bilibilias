package com.imcys.bilibilias.common.base.extend

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.get
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

internal suspend inline fun <reified T> HttpClient.safeGet(
    url: String,
    crossinline block: HttpRequestBuilder.() -> Unit = {},
): Flow<Result<T>> =
    httpBlock { get(url, block).body() }

private fun <T> httpBlock(block: suspend () -> T): Flow<Result<T>> =
    flow { emit(block()) }.asResult()

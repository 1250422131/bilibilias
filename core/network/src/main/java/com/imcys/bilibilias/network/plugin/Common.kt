package com.imcys.bilibilias.network.plugin


import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.statement.HttpResponse


fun HttpRequestBuilder.isSSE(): Boolean {
    val contentType = this.headers["Content-Type"] ?: return false
    return contentType.contains("text/event-stream")
}

fun HttpResponse.isSSE(): Boolean {
    val contentType = this.headers["Content-Type"] ?: return false
    return contentType.contains("text/event-stream")
}
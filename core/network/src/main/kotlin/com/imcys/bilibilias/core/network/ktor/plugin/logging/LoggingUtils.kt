package com.imcys.bilibilias.core.network.ktor.plugin.logging

import io.ktor.client.statement.HttpResponse
import io.ktor.http.ContentType
import io.ktor.http.ContentType.Application.HalJson
import io.ktor.http.ContentType.Application.Json
import io.ktor.http.HeaderValueParam
import io.ktor.http.charset
import io.ktor.utils.io.ByteReadChannel
import io.ktor.utils.io.charsets.Charset
import io.ktor.utils.io.charsets.Charsets
import io.ktor.utils.io.core.readText

internal fun Appendable.logHeaders(
    headers: Set<Map.Entry<String, List<String>>>,
    sanitizedHeaders: List<SanitizedHeader>,
) {
    val sortedHeaders = headers.toList().sortedBy { it.key }

    sortedHeaders.forEach { (key, values) ->
        val placeholder = sanitizedHeaders.firstOrNull { it.predicate(key) }?.placeholder
        logHeader(key, placeholder ?: values.joinToString("; "))
    }
}

internal fun Appendable.logHeader(key: String, value: String) {
    appendLine("-> $key: $value")
}

internal fun logResponseHeader(
    log: StringBuilder,
    response: HttpResponse,
    level: JsonAwareLogLevel,
    sanitizedHeaders: List<SanitizedHeader>,
) {
    with(log) {
        if (level.info) {
            appendLine("RESPONSE: ${response.status}")
            appendLine("METHOD: ${response.call.request.method}")
            appendLine("FROM: ${response.call.request.url}")
        }

        if (level.headers) {
            appendLine("COMMON HEADERS")
            logHeaders(response.headers.entries(), sanitizedHeaders)
        }
    }
}

internal suspend inline fun ByteReadChannel.tryReadText(charset: Charset): String? =
    try {
        readRemaining().readText(charset = charset)
    } catch (cause: Throwable) {
        null
    }

internal suspend fun logResponseBody(
    log: StringBuilder,
    contentType: ContentType?,
    content: ByteReadChannel,
    logger: JsonAwareLogger,
) {
    with(log) {
        appendLine("BODY Content-Type: $contentType")
        appendLine("BODY START")

        val message = content.tryReadText(contentType?.charset() ?: Charsets.UTF_8)
            ?.prettifyJsonIfNeeded(contentType, logger)
            ?: "[response body omitted]"
        appendLine(message)

        append("BODY END")
    }
}

private val JsonUtf8 =
    ContentType("application", "json", parameters = listOf(HeaderValueParam("charset", "utf-8")))

internal fun String.prettifyJsonIfNeeded(contentType: ContentType?, logger: JsonAwareLogger) =
    if (contentType == JsonUtf8 || contentType == Json || contentType == HalJson) {
        logger.prettifyJson(this)
    } else {
        this
    }

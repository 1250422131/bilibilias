package com.imcys.bilibilias.core.network.ktor.plugin.logging

import io.ktor.client.HttpClient
import io.ktor.client.HttpClientConfig
import io.ktor.client.call.HttpClientCall
import io.ktor.client.plugins.api.ClientHook
import io.ktor.client.plugins.api.ClientPlugin
import io.ktor.client.plugins.api.createClientPlugin
import io.ktor.client.plugins.observer.ResponseHandler
import io.ktor.client.plugins.observer.ResponseObserver
import io.ktor.client.request.HttpRequest
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.HttpSendPipeline
import io.ktor.client.statement.HttpReceivePipeline
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.HttpResponseContainer
import io.ktor.client.statement.HttpResponsePipeline
import io.ktor.http.HttpHeaders
import io.ktor.http.Url
import io.ktor.http.charset
import io.ktor.http.content.OutgoingContent
import io.ktor.http.contentType
import io.ktor.util.AttributeKey
import io.ktor.util.InternalAPI
import io.ktor.util.KtorDsl
import io.ktor.util.pipeline.PipelineContext
import io.ktor.utils.io.ByteChannel
import io.ktor.utils.io.charsets.Charsets
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

private val ClientJsonAwareCallLogger = AttributeKey<HttpClientJsonAwareCallLogger>("JsonAwareCallLogger")
private val DisableJsonAwareLogging = AttributeKey<Unit>("DisableJsonAwareLogging")

/**
 * A configuration for the [JsonAwareLogging] plugin.
 */
@KtorDsl
public class JsonAwareLoggingConfig {
    internal var filters = mutableListOf<(HttpRequestBuilder) -> Boolean>()
    internal val sanitizedHeaders = mutableListOf<SanitizedHeader>()

    private var _logger: JsonAwareLogger? = null

    /**
     * Specifies a [JsonAwareLogger] instance.
     */
    public var logger: JsonAwareLogger
        get() = _logger ?: JsonAwareLogger.DEFAULT
        set(value) {
            _logger = value
        }

    /**
     * Specifies the logging level.
     */
    public var level: JsonAwareLogLevel = JsonAwareLogLevel.BODY

    /**
     * Allows you to filter log messages for calls matching a [predicate].
     */
    public fun filter(predicate: (HttpRequestBuilder) -> Boolean) {
        filters.add(predicate)
    }

    /**
     * Allows you to sanitize sensitive headers to avoid their values appearing in the logs.
     * In the example below, Authorization header value will be replaced with '***' when logging:
     * ```kotlin
     * sanitizeHeader { header -> header == HttpHeaders.Authorization }
     * ```
     */
    public fun sanitizeHeader(placeholder: String = "***", predicate: (String) -> Boolean) {
        sanitizedHeaders.add(SanitizedHeader(placeholder, predicate))
    }
}

/**
 * A client's plugin that provides the capability to log HTTP calls.
 */
public val JsonAwareLogging: ClientPlugin<JsonAwareLoggingConfig> =
    createClientPlugin("JsonAwareLogging", ::JsonAwareLoggingConfig) {
        val logger: JsonAwareLogger = pluginConfig.logger
        val level: JsonAwareLogLevel = pluginConfig.level
        val filters: List<(HttpRequestBuilder) -> Boolean> = pluginConfig.filters
        val sanitizedHeaders: List<SanitizedHeader> = pluginConfig.sanitizedHeaders

        fun shouldBeLogged(request: HttpRequestBuilder): Boolean = filters.isEmpty() || filters.any { it(request) }

        @OptIn(DelicateCoroutinesApi::class)
        suspend fun logRequestBody(
            content: OutgoingContent,
            logger: HttpClientJsonAwareCallLogger,
        ): OutgoingContent {
            val requestLog = StringBuilder()

            val contentType = content.contentType
            val charset = contentType?.charset() ?: Charsets.UTF_8
            val channel = ByteChannel()

            requestLog.appendLine("BODY Content-Type: $contentType")

            GlobalScope.launch(Dispatchers.Unconfined) {
                val body = channel.tryReadText(charset)
                    ?.prettifyJsonIfNeeded(contentType, this@createClientPlugin.pluginConfig.logger)
                    ?: "[request body omitted]"
                requestLog.appendLine("BODY START")
                requestLog.appendLine(body)
                requestLog.append("BODY END")
            }.invokeOnCompletion {
                logger.logRequest(requestLog.toString())
                logger.closeRequestLog()
            }

            return content.observe(channel)
        }

        fun logRequestException(context: HttpRequestBuilder, cause: Throwable) {
            if (level.info) {
                logger.log("REQUEST ${Url(context.url)} failed with exception: $cause")
            }
        }

        suspend fun logRequest(request: HttpRequestBuilder): OutgoingContent? {
            val content = request.body as OutgoingContent
            val callLogger = HttpClientJsonAwareCallLogger(logger)

            request.attributes.put(ClientJsonAwareCallLogger, callLogger)

            val message = buildString {
                if (level.info) {
                    appendLine("REQUEST: ${Url(request.url)}")
                    appendLine("METHOD: ${request.method}")
                }

                if (level.headers) {
                    appendLine("COMMON HEADERS")
                    logHeaders(request.headers.entries(), sanitizedHeaders)

                    appendLine("CONTENT HEADERS")
                    val contentLengthPlaceholder = sanitizedHeaders
                        .firstOrNull { it.predicate(HttpHeaders.ContentLength) }
                        ?.placeholder
                    val contentTypePlaceholder = sanitizedHeaders
                        .firstOrNull { it.predicate(HttpHeaders.ContentType) }
                        ?.placeholder
                    content.contentLength?.let {
                        logHeader(HttpHeaders.ContentLength, contentLengthPlaceholder ?: it.toString())
                    }
                    content.contentType?.let {
                        logHeader(HttpHeaders.ContentType, contentTypePlaceholder ?: it.toString())
                    }
                    logHeaders(content.headers.entries(), sanitizedHeaders)
                }
            }

            if (message.isNotEmpty()) {
                callLogger.logRequest(message)
            }

            if (message.isEmpty() || !level.body) {
                callLogger.closeRequestLog()
                return null
            }

            return logRequestBody(content, callLogger)
        }

        fun logResponseException(log: StringBuilder, request: HttpRequest, cause: Throwable) {
            if (!level.info) return
            log.append("RESPONSE ${request.url} failed with exception: $cause")
        }

        on(SendHook) { request ->
            if (!shouldBeLogged(request)) {
                request.attributes.put(DisableJsonAwareLogging, Unit)
                return@on
            }

            val loggedRequest = try {
                logRequest(request)
            } catch (_: Throwable) {
                null
            }

            try {
                proceedWith(loggedRequest ?: request.body)
            } catch (cause: Throwable) {
                logRequestException(request, cause)
                throw cause
            } finally {
            }
        }

        on(ResponseHook) { response ->
            if (level == JsonAwareLogLevel.NONE || response.call.attributes.contains(DisableJsonAwareLogging)) return@on

            val callLogger = response.call.attributes[ClientJsonAwareCallLogger]
            val header = StringBuilder()

            var failed = false

            try {
                logResponseHeader(header, response.call.response, level, sanitizedHeaders)
                proceed()
            } catch (cause: Throwable) {
                logResponseException(header, response.call.request, cause)
                failed = true
                throw cause
            } finally {
                callLogger.logResponseHeader(header.toString())
                if (failed || !level.body) callLogger.closeResponseLog()
            }
        }

        on(ReceiveHook) { call ->
            if (level == JsonAwareLogLevel.NONE || call.attributes.contains(DisableJsonAwareLogging)) {
                return@on
            }

            try {
                proceed()
            } catch (cause: Throwable) {
                val log = StringBuilder()
                val callLogger = call.attributes[ClientJsonAwareCallLogger]
                logResponseException(log, call.request, cause)
                callLogger.logResponseException(log.toString())
                callLogger.closeResponseLog()
                throw cause
            }
        }

        if (!level.body) return@createClientPlugin

        @OptIn(InternalAPI::class)
        val observer: ResponseHandler = observer@{
            if (level == JsonAwareLogLevel.NONE || it.call.attributes.contains(DisableJsonAwareLogging)) {
                return@observer
            }

            val callLogger = it.call.attributes[ClientJsonAwareCallLogger]
            val log = StringBuilder()

            try {
                logResponseBody(log, it.contentType(), it.content, logger)
            } catch (_: Throwable) {
            } finally {
                callLogger.logResponseBody(log.toString().trim())
                callLogger.closeResponseLog()
            }
        }

        ResponseObserver.install(ResponseObserver.prepare { onResponse(observer) }, client)
    }

/**
 * Configures and installs [JsonAwareLogging] in [HttpClient].
 */
@Suppress("FunctionName")
public fun HttpClientConfig<*>.JsonAwareLogging(block: JsonAwareLoggingConfig.() -> Unit = {}) {
    install(JsonAwareLogging, block)
}

internal class SanitizedHeader(
    val placeholder: String,
    val predicate: (String) -> Boolean,
)

private object ResponseHook : ClientHook<suspend ResponseHook.Context.(response: HttpResponse) -> Unit> {
    class Context(private val context: PipelineContext<HttpResponse, Unit>) {
        suspend fun proceed() = context.proceed()
    }

    override fun install(
        client: HttpClient,
        handler: suspend Context.(response: HttpResponse) -> Unit,
    ) {
        client.receivePipeline.intercept(HttpReceivePipeline.State) {
            handler(Context(this), subject)
        }
    }
}

private object SendHook : ClientHook<suspend SendHook.Context.(response: HttpRequestBuilder) -> Unit> {
    class Context(private val context: PipelineContext<Any, HttpRequestBuilder>) {
        suspend fun proceedWith(content: Any) = context.proceedWith(content)
    }

    override fun install(
        client: HttpClient,
        handler: suspend Context.(request: HttpRequestBuilder) -> Unit,
    ) {
        client.sendPipeline.intercept(HttpSendPipeline.Monitoring) {
            handler(Context(this), context)
        }
    }
}

private object ReceiveHook : ClientHook<suspend ReceiveHook.Context.(call: HttpClientCall) -> Unit> {
    class Context(private val context: PipelineContext<HttpResponseContainer, HttpClientCall>) {
        suspend fun proceed() = context.proceed()
    }

    override fun install(
        client: HttpClient,
        handler: suspend Context.(call: HttpClientCall) -> Unit,
    ) {
        client.responsePipeline.intercept(HttpResponsePipeline.Receive) {
            handler(Context(this), context)
        }
    }
}

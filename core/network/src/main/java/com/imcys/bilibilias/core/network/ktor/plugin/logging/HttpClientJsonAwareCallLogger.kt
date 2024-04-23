package com.imcys.bilibilias.core.network.ktor.plugin.logging

import kotlinx.atomicfu.atomic
import kotlinx.coroutines.Job

internal class HttpClientJsonAwareCallLogger(private val logger: JsonAwareLogger) {
    private val requestLog = StringBuilder()
    private val responseLog = StringBuilder()
    private val requestLoggedMonitor = Job()
    private val responseHeaderMonitor = Job()

    private val requestLogged = atomic(false)
    private val responseLogged = atomic(false)

    fun logRequest(message: String) {
        requestLog.appendLine(message.trim())
    }

    fun logResponseHeader(message: String) {
        responseLog.appendLine(message.trim())
        responseHeaderMonitor.complete()
    }

    suspend fun logResponseException(message: String) {
        requestLoggedMonitor.join()
        logger.log(message.trim())
    }

    suspend fun logResponseBody(message: String) {
        responseHeaderMonitor.join()
        responseLog.append(message)
    }

    fun closeRequestLog() {
        if (!requestLogged.compareAndSet(expect = false, update = true)) return

        try {
            val message = requestLog.trim().toString()
            if (message.isNotEmpty()) logger.log(message)
        } finally {
            requestLoggedMonitor.complete()
        }
    }

    suspend fun closeResponseLog() {
        if (!responseLogged.compareAndSet(expect = false, update = true)) return
        requestLoggedMonitor.join()

        val message = responseLog.trim().toString()
        if (message.isNotEmpty()) logger.log(message)
    }
}

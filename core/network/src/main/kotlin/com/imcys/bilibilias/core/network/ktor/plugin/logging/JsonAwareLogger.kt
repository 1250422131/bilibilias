package com.imcys.bilibilias.core.network.ktor.plugin.logging

import io.ktor.client.HttpClient
import org.slf4j.LoggerFactory

/**
 * [HttpClient] Logger.
 */
public interface JsonAwareLogger {
    /**
     * Add [message] to log.
     */
    public fun log(message: String)

    /**
     * Format the [json] pretty.
     */
    public fun prettifyJson(message: String): String = message

    public companion object
}

/**
 * Default logger to use.
 */
public val JsonAwareLogger.Companion.DEFAULT: JsonAwareLogger
    get() = object : JsonAwareLogger {
        private val delegate = LoggerFactory.getLogger(HttpClient::class.java)!!
        override fun log(message: String) {
            delegate.info(message)
        }
    }

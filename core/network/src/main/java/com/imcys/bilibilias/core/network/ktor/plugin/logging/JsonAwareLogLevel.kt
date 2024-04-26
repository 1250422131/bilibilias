package com.imcys.bilibilias.core.network.ktor.plugin.logging

/**
 * [JsonAwareLogging] log level.
 */
public enum class JsonAwareLogLevel(
    public val info: Boolean,
    public val headers: Boolean,
    public val body: Boolean,
) {
    ALL(info = true, headers = true, body = true),
    HEADERS(info = true, headers = true, body = false),
    BODY(info = true, headers = false, body = true),
    INFO(info = true, headers = false, body = false),
    NONE(info = false, headers = false, body = false),
}

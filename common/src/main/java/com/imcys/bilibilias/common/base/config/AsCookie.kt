package com.imcys.bilibilias.common.base.config

import io.ktor.http.Cookie
import io.ktor.http.CookieEncoding
import kotlinx.serialization.Serializable

@Serializable
data class AsCookie(
    val name: String,
    val value: String,
    val encoding: CookieEncoding = CookieEncoding.URI_ENCODING,
    val maxAge: Int? = null,
    val timestamp: Long? = 0,
    val domain: String? = null,
    val path: String? = null,
    val secure: Boolean = false,
    val httpOnly: Boolean = false,
)

fun Cookie.toAsCookie(): AsCookie =
    AsCookie(
        name = name,
        value = value,
        encoding = encoding,
        maxAge = maxAge,
        timestamp = expires?.timestamp,
        domain = domain,
        path = path,
        secure = secure,
        httpOnly = httpOnly
    )

fun AsCookie.toCookie(): Cookie =
    Cookie(
        name = name,
        value = value,
        encoding = encoding,
        maxAge = maxAge,
        domain = domain,
        path = path,
        secure = secure,
        httpOnly = httpOnly
    )

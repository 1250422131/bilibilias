package com.imcys.bilibilias.core.datastore.model

import kotlinx.serialization.Serializable

@Serializable
data class AsCookieStore(val cookies: Map<String, Cookie> = emptyMap())

@Serializable
public data class Cookie(
    val name: String,
    val value: String,
    val maxAge: Int = 0,
    val timestamp: Long? = null,
    val domain: String? = null,
    val path: String? = null,
    val secure: Boolean = false,
    val httpOnly: Boolean = false,
)

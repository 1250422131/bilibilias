package com.imcys.model.login

import kotlinx.serialization.Serializable

@Serializable
data class Cookie(
    val name: String,
    val value: String,
    val maxAge: Int? = null,
    val timestamp: Long? = null,
    val domain: String? = null,
    val path: String? = null,
    val secure: Boolean = false,
    val httpOnly: Boolean = false,
)

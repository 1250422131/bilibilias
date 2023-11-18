package com.imcys.model.cookie

import kotlinx.serialization.Serializable

@Serializable
data class AsCookie(
    val name: String,
    val value: String,
    val maxAge: Int = 0,
    val timestamp: Long,
    val domain: String? = null,
    val path: String? = null,
    val secure: Boolean = false,
    val httpOnly: Boolean = false,
)

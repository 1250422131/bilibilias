package com.imcys.model.login

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CookieState(
        @SerialName("refresh")
        val refresh: Boolean = false,
        @SerialName("timestamp")
        val timestamp: Long = 0
    )
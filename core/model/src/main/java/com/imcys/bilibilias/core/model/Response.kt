package com.imcys.bilibilias.core.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Response(
    @SerialName("data")
    val `data`: Int = 0,
    @SerialName("message")
    val message: String = "",
)

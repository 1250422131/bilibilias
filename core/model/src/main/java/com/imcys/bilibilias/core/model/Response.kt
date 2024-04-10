package com.imcys.bilibilias.core.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Response(
    @SerialName("data")
    val `data`: Int = Int.MIN_VALUE,
    @SerialName("message")
    val message: String = "",
) {
    val success = data == 0
}

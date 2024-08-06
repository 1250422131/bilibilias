package com.imcys.bilibilias.core.model.login

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class QrcodePoll(
    @SerialName("code")
    val code: Int = 0,
    @SerialName("message")
    val message: String = "",
    @SerialName("refresh_token")
    val refreshToken: String = "",
    @SerialName("timestamp")
    val timestamp: Long = 0,
    @SerialName("url")
    val url: String = "",
) {
    val success get() = code == 0
}

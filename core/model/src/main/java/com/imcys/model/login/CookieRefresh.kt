package com.imcys.model.login

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CookieRefresh(
    @SerialName("message")
    val message: String = "",
    @SerialName("refresh_token")
    val refreshToken: String = "",
    @SerialName("status")
    val status: Int = 0
)

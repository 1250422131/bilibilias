package com.imcys.model.login

import kotlinx.serialization.*

/**
 * ![二维码登录](https://github.com/SocialSisterYi/bilibili-API-collect/blob/master/docs/login/login_action/QR.md)
 */
@Serializable
data class LoginResponse(
    @SerialName("code")
    val code: Int = 0, // 86038
    @SerialName("message")
    val message: String = "", // 二维码已失效
    @SerialName("refresh_token")
    val refreshToken: String = "",
    @SerialName("timestamp")
    val timestamp: Long = 0, // 0
    @SerialName("url")
    val url: String = ""
) {
    /**
     * 密钥超时为180秒
     */
    val isSuccess: Boolean
        get() = code == 0
}

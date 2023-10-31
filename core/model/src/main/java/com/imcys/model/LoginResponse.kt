package com.imcys.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * ![二维码登录](https://github.com/SocialSisterYi/bilibili-API-collect/blob/master/docs/login/login_action/QR.md)
 *
 * ```json
 * {
 *   "code": 0,
 *   "message": "0",
 *   "ttl": 1,
 *   "data": {
 *     "url": "",
 *     "refresh_token": "",
 *     "timestamp": 0,
 *     "code": 86101,
 *     "message": "未扫码"
 *   }
 * }
 * ```
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

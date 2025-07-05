package com.imcys.bilibilias.network.model


import com.imcys.bilibilias.network.model.TvQRCodePollInfo.CookieInfo
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class QRCodePollInfo(
    @SerialName("code")
    val code: Int,
    @SerialName("message")
    val message: String,
    @SerialName("refresh_token")
    val refreshToken: String,
    @SerialName("timestamp")
    val timestamp: Long,
    @SerialName("url")
    val url: String,
    @Transient
    val accessToken: String = "",
    @Transient
    val cookie: String = "",
    @Transient
    val cookieInfo: CookieInfo? = null
)


@Serializable
data class TvQRCodePollInfo(
    val mid: Long = 0,
    @SerialName("access_token")
    val accessToken: String = "",
    @SerialName("refresh_token")
    val refreshToken: String = "",
    @SerialName("expires_in")
    val expiresIn: Long = 0,
    @SerialName("cookie_info")
    val cookieInfo: CookieInfo,
) {
    @Serializable
    data class CookieInfo(
        @SerialName("cookies")
        val cookies: List<Cookies>,
        @SerialName("domains")
        val domains: List<String>
    ) {
        @Serializable
        data class Cookies(
            @SerialName("expires")
            val expires: Long,
            @SerialName("http_only")
            val httpOnly: Long,
            @SerialName("name")
            val name: String,
            @SerialName("secure")
            val secure: Long,
            @SerialName("value")
            val value: String
        )
    }
}
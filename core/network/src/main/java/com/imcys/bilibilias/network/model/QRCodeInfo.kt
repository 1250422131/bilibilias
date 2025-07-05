package com.imcys.bilibilias.network.model


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class QRCodeInfo(
    @SerialName("qrcode_key")
    val qrcodeKey: String,
    @SerialName("url")
    val url: String
)

/**
 * TV扫码接口
 */
@Serializable
data class TVQRCodeInfo(
    @SerialName("auth_code")
    val authCode: String,
    @SerialName("url")
    val url: String
)

package com.imcys.bilibilias.core.datasource.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class QrCode(
    @SerialName("qrcode_key")
    val qrcodeKey: String = "",
    @SerialName("url")
    val url: String = ""
)
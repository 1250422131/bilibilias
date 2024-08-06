package com.imcys.bilibilias.core.model.login

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class QrcodeGenerate(
    @SerialName("qrcode_key")
    val qrcodeKey: String = "",
    @SerialName("url")
    val url: String = "",
)

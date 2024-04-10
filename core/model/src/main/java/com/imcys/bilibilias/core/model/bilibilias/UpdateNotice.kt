package com.imcys.bilibilias.core.model.bilibilias

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UpdateNotice(
    @SerialName("notice")
    val notice: String = "",
    @SerialName("url")
    val url: String = "",
    @SerialName("version")
    val version: String = "",
    @SerialName("ID")
    val id: String = "",
    @SerialName("gxnotice")
    val gxNotice: String = "",
    @SerialName("APKMD5")
    val md5: String = "",
    @SerialName("APKToKen")
    val token: String = "",
    @SerialName("APKToKenCR")
    val cr: String = "",
)

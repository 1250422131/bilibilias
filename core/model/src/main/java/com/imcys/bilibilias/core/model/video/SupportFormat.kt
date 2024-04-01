package com.imcys.bilibilias.core.model.video

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SupportFormat(
    @SerialName("codecs")
    val codecs: List<String> = listOf(),
    @SerialName("description")
    val description: String = "",
    @SerialName("display_desc")
    val displayDesc: String = "",
    @SerialName("format")
    val format: String = "",
    @SerialName("need_login")
    val needLogin: Boolean = false,
    @SerialName("need_vip")
    val needVip: Boolean = false,
    @SerialName("new_description")
    val newDescription: String = "",
    @SerialName("quality")
    val quality: Int = 0,
    @SerialName("sub_description")
    val subDescription: String = "",
    @SerialName("superscript")
    val superscript: String = ""
)

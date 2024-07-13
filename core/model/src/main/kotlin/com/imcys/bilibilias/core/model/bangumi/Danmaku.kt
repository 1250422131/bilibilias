package com.imcys.bilibilias.core.model.bangumi

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Danmaku(
    @SerialName("icon")
    val icon: String = "",
    @SerialName("pure_text")
    val pureText: String = "",
    @SerialName("text")
    val text: String = "",
    @SerialName("value")
    val value: Int = 0
)

package com.imcys.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SupportFormat(
    @SerialName("codecs")
    val codecs: List<String> = emptyList(), // null
    @SerialName("format")
    val format: String = "", // mp4
    @SerialName("new_description")
    val newDescription: String = "", // 1080P 高清
    @SerialName("quality")
    val quality: Int = 0, // 80
)

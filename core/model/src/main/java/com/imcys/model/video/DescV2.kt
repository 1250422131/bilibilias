package com.imcys.model.video

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class DescV2(
    @SerialName("biz_id")
    val bizId: Int = 0,
    @SerialName("raw_text")
    val rawText: String = "",
    @SerialName("type")
    val type: Int = 0
)

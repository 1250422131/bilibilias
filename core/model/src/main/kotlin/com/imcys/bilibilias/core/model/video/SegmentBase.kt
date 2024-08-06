package com.imcys.bilibilias.core.model.video

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SegmentBase(
    @SerialName("index_range")
    val indexRange: String = "",
    @SerialName("initialization")
    val initialization: String = "",
)

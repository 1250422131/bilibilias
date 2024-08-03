package com.imcys.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Dimension(
    @SerialName("height")
    val height: Int = 0,
    @SerialName("rotate")
    val rotate: Int = 0,
    @SerialName("width")
    val width: Int = 0
)

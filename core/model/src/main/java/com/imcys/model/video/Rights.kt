package com.imcys.model.video

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Rights(
    @SerialName("hd5")
    val hd5: Int = 0,
    @SerialName("is_360")
    val is360: Int = 0,
    @SerialName("is_stein_gate")
    val isSteinGate: Int = 0,
    @SerialName("movie")
    val movie: Int = 0,
)
package com.imcys.model.video

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Author(
    @SerialName("mid") val mid: Long = 0,
    @SerialName("name") val name: String = "",
    @SerialName("face") val face: String = "",
    @SerialName("sign") val sign: String = "",
    @SerialName("rank") val rank: Int = 0,
)
    
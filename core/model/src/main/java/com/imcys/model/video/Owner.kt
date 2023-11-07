package com.imcys.model.video

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Owner(
    @SerialName("face")
    val face: String = "",
    @SerialName("mid")
    val mid: Long = 0,
    @SerialName("name")
    val name: String = ""
)

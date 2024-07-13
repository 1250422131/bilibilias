package com.imcys.bilibilias.core.model.user

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Pendant(
    @SerialName("expire")
    val expire: Int = 0,
    @SerialName("image")
    val image: String = "",
    @SerialName("image_enhance")
    val imageEnhance: String = "",
    @SerialName("image_enhance_frame")
    val imageEnhanceFrame: String = "",
    @SerialName("n_pid")
    val nPid: Int = 0,
    @SerialName("name")
    val name: String = "",
    @SerialName("pid")
    val pid: Int = 0
)

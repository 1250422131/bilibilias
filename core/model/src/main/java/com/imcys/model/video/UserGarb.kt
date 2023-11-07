package com.imcys.model.video

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserGarb(
    @SerialName("url_image_ani_cut")
    val urlImageAniCut: String = ""
)
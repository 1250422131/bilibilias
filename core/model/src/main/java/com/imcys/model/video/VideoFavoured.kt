package com.imcys.model.video

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class VideoFavoured(
    @SerialName("favoured")
    val isFavoured: Boolean = false
)

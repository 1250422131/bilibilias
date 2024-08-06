package com.imcys.bilibilias.core.model.video

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Dash(
    @SerialName("audio")
    val audio: List<Sources> = listOf(),
    @SerialName("duration")
    val duration: Int = 0,
    @SerialName("min_buffer_time")
    val minBufferTime: Double = 0.0,
    @SerialName("video")
    val video: List<Sources> = listOf(),
)

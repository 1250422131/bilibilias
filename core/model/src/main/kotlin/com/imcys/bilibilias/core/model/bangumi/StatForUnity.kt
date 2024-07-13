package com.imcys.bilibilias.core.model.bangumi

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class StatForUnity(
    @SerialName("coin")
    val coin: Int = 0,
    @SerialName("danmaku")
    val danmaku: Danmaku = Danmaku(),
    @SerialName("likes")
    val likes: Int = 0,
    @SerialName("reply")
    val reply: Int = 0,
)

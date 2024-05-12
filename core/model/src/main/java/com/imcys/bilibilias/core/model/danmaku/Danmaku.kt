package com.imcys.bilibilias.core.model.danmaku

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Danmaku(
    @SerialName("content")
    val content: String = "",
    @SerialName("createTime")
    val createTime: Long = 0,
    @SerialName("danmakuId")
    val danmakuId: Long = 0,
    @SerialName("danmakuType")
    val danmakuType: Int = 0,
    @SerialName("mode")
    val mode: Int = 0,
    @SerialName("position")
    val position: Long = 0,
    @SerialName("rank")
    val rank: Int = 0,
    @SerialName("roleId")
    val roleId: Int = 0,
    @SerialName("textColor")
    val textColor: Int = 0,
    @SerialName("textSize")
    val textSize: Int = 0,
    @SerialName("userId")
    val userId: Long = 0
)

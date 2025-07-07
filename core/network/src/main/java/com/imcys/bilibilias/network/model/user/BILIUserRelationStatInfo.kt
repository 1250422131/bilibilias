package com.imcys.bilibilias.network.model.user


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * 关注和被关注数量信息
 */
@Serializable
data class BILIUserRelationStatInfo(
    @SerialName("black")
    val black: Long = 0,
    @SerialName("follower")
    val follower: Long = 0,
    @SerialName("following")
    val following: Long = 0,
    @SerialName("mid")
    val mid: Long = 0,
    @SerialName("whisper")
    val whisper: Long = 0
)
package com.imcys.bilibilias.core.model.video
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
@Serializable
data class Stat(
    @SerialName("aid")
    val aid: Long = 0,
    @SerialName("coin")
    val coin: Int = 0,
    @SerialName("danmaku")
    val danmaku: Int = 0,
    @SerialName("dislike")
    val dislike: Int = 0,
    @SerialName("evaluation")
    val evaluation: String = "",
    @SerialName("favorite")
    val favorite: Int = 0,
    @SerialName("his_rank")
    val hisRank: Int = 0,
    @SerialName("like")
    val like: Int = 0,
    @SerialName("now_rank")
    val nowRank: Int = 0,
    @SerialName("reply")
    val reply: Int = 0,
    @SerialName("share")
    val share: Int = 0,
    @SerialName("view")
    val view: Int = 0,
    @SerialName("vt")
    val vt: Int = 0
)

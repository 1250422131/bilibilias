package com.imcys.model.video

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class HonorReply(
    @SerialName("honor")
    val honor: List<Honor> = listOf()
)

@Serializable
data class Honor(
    @SerialName("aid")
    val aid: Int = 0,
    @SerialName("desc")
    val desc: String = "",
    @SerialName("type")
    val type: Int = 0,
    @SerialName("weekly_recommend_num")
    val weeklyRecommendNum: Int = 0
)
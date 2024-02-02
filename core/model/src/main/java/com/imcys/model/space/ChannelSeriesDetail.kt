package com.imcys.model.space

import com.imcys.model.Page
import com.imcys.model.video.Stat
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ChannelSeriesDetail(
    @SerialName("meta")
    val meta: Meta = Meta(),
    @SerialName("recent_aids")
    val recentAids: List<Int> = listOf()
)

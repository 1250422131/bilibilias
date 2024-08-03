package com.imcys.model.space

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SpaceChannelList(
    @SerialName("count")
    val count: Int,
    @SerialName("list")
    val list: List<InnerList>
) {
    @Serializable
    data class InnerList(
        @SerialName("cid")
        val channelId: Long,
        @SerialName("count")
        val count: Int,
        @SerialName("cover")
        val cover: String,
        @SerialName("intro")
        val intro: String,
        @SerialName("mid")
        val mid: Long,
        @SerialName("mtime")
        val mtime: Int,
        @SerialName("name")
        val name: String,
        @SerialName("is_live_playback")
        val isLivePlayback: Boolean = false
    )
}

package com.imcys.model.space

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

data class SpaceChannelList(
    @SerialName("count")
    val count: Int,
    @SerialName("list")
    val list: List<InnerList>
) {
    @Serializable
    data class InnerList(
        @SerialName("cid")
        val cid: Long,
        @SerialName("count")
        val count: Int,
        @SerialName("cover")
        val cover: String,
        @SerialName("intro")
        val intro: String,
        @SerialName("mid")
        val mid: Int,
        @SerialName("mtime")
        val mtime: Int,
        @SerialName("name")
        val name: String
    )
}

package com.imcys.model.space

import com.imcys.model.Page
import com.imcys.model.video.Archive
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SpaceChannelVideo(
    @SerialName("list")
    val list: InnerList = InnerList(),
    @SerialName("page")
    val page: Page = Page()
) {
    @Serializable
    data class InnerList(
        @SerialName("archives")
        val archives: List<Archive> = listOf(),
        @SerialName("cid")
        val channelId: Long = 0,
        @SerialName("count")
        val count: Int = 0,
        @SerialName("cover")
        val cover: String = "",
        @SerialName("intro")
        val intro: String = "",
        @SerialName("mid")
        val mid: Long = 0,
        @SerialName("mtime")
        val mtime: Int = 0,
        @SerialName("name")
        val name: String = ""
    )
}

package com.imcys.model.space

import com.imcys.model.Dimension
import com.imcys.model.Page
import com.imcys.model.video.Owner
import com.imcys.model.video.Rights
import com.imcys.model.video.Stat
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
        val cid: Long = 0,
        @SerialName("count")
        val count: Int = 0,
        @SerialName("cover")
        val cover: String = "",
        @SerialName("intro")
        val intro: String = "",
        @SerialName("mid")
        val mid: Int = 0,
        @SerialName("mtime")
        val mtime: Int = 0,
        @SerialName("name")
        val name: String = ""
    ) {
        @Serializable
        data class Archive(
            @SerialName("aid")
            val aid: Long = 0,
            @SerialName("bvid")
            val bvid: String = "",
            @SerialName("cid")
            val cid: Int = 0,
            @SerialName("ctime")
            val ctime: Int = 0,
            @SerialName("desc")
            val desc: String = "",
            @SerialName("dimension")
            val dimension: Dimension = Dimension(),
            @SerialName("duration")
            val duration: Int = 0,
            @SerialName("dynamic")
            val `dynamic`: String = "",
            @SerialName("inter_video")
            val interVideo: Boolean = false,
            @SerialName("owner")
            val owner: Owner = Owner(),
            @SerialName("pic")
            val pic: String = "",
            @SerialName("pubdate")
            val pubdate: Int = 0,
            @SerialName("rights")
            val rights: Rights = Rights(),
            @SerialName("stat")
            val stat: Stat = Stat(),
            @SerialName("state")
            val state: Int = 0,
            @SerialName("tid")
            val tid: Int = 0,
            @SerialName("title")
            val title: String = "",
            @SerialName("videos")
            val videos: Int = 0
        )
    }
}
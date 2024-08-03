package com.imcys.model.video

import com.imcys.model.Dimension
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Archive(
    @SerialName("aid")
    val aid: Long = 0,
    @SerialName("bvid")
    val bvid: String = "",
    @SerialName("cid")
    val cid: Long = 0,
    @SerialName("ctime")
    val ctime: Long = 0,
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
    @SerialName("title")
    val title: String = "",
    @SerialName("videos")
    val videos: Int = 0
)

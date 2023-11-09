package com.imcys.model

import com.imcys.model.video.Owner
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
        val cid: Int = 0,
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
            val aid: Int = 0,
            @SerialName("attribute")
            val attribute: Int = 0,
            @SerialName("bvid")
            val bvid: String = "",
            @SerialName("cid")
            val cid: Int = 0,
            @SerialName("copyright")
            val copyright: Int = 0,
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
            @SerialName("mission_id")
            val missionId: Int? = 0,
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
            @SerialName("tname")
            val tname: String = "",
            @SerialName("videos")
            val videos: Int = 0
        ) {

            @Serializable
            data class Rights(
                @SerialName("autoplay")
                val autoplay: Int = 0,
                @SerialName("bp")
                val bp: Int = 0,
                @SerialName("download")
                val download: Int = 0,
                @SerialName("elec")
                val elec: Int = 0,
                @SerialName("hd5")
                val hd5: Int = 0,
                @SerialName("is_cooperation")
                val isCooperation: Int = 0,
                @SerialName("movie")
                val movie: Int = 0,
                @SerialName("no_background")
                val noBackground: Int = 0,
                @SerialName("no_reprint")
                val noReprint: Int = 0,
                @SerialName("pay")
                val pay: Int = 0,
                @SerialName("ugc_pay")
                val ugcPay: Int = 0,
                @SerialName("ugc_pay_preview")
                val ugcPayPreview: Int = 0
            )

            @Serializable
            data class Stat(
                @SerialName("aid")
                val aid: Int = 0,
                @SerialName("coin")
                val coin: Int = 0,
                @SerialName("danmaku")
                val danmaku: Int = 0,
                @SerialName("dislike")
                val dislike: Int = 0,
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
                val view: Int = 0
            )
        }
    }

    @Serializable
    data class Page(
        @SerialName("count")
        val count: Int = 0,
        @SerialName("num")
        val num: Int = 0,
        @SerialName("size")
        val size: Int = 0
    )
}
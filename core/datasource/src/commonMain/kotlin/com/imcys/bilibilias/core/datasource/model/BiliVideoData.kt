package com.imcys.bilibilias.core.datasource.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class BiliVideoData(
    @SerialName("bvid") val bvid: String = "",
    @SerialName("aid") val aid: Long = 0,
    @SerialName("videos") val videos: Int = 0,
    @SerialName("tid") val tid: Long = 0,
    @SerialName("tname") val tName: String = "",
    @SerialName("copyright") val copyright: Int = 0,
    @SerialName("pic") val pic: String = "",
    @SerialName("title") val title: String = "",
    @SerialName("pubdate") val pubDate: Long = 0,
    @SerialName("ctime") val cTime: Long = 0,
    @SerialName("desc") val desc: String = "",
    @SerialName("state") val state: Int = 0,
    @SerialName("duration") val duration: Long = 0,
    @SerialName("rights") val rights: BiliVideoRights = BiliVideoRights(),
    @SerialName("owner") val owner: BiliVideoOwner = BiliVideoOwner(),
    @SerialName("stat") val stat: BiliVideoStat = BiliVideoStat(),
    @SerialName("dynamic") val dynamic: String = "",
    @SerialName("cid") val cid: Long = 0,
    @SerialName("dimension") val dimension: BiliVideoDimension = BiliVideoDimension(),
    @SerialName("pages") val pages: List<BiliVideoPage> = emptyList(),
    @SerialName("subtitle") val subtitle: BiliVideoSubtitle = BiliVideoSubtitle(),
    @SerialName("staff") val staff: List<BiliVideoStaff> = emptyList()
)

@Serializable
data class BiliVideoRights(
    @SerialName("bp") val bp: Int = 0,
    @SerialName("elec") val elec: Int = 0,
    @SerialName("download") val download: Int = 0,
    @SerialName("movie") val movie: Int = 0,
    @SerialName("pay") val pay: Int = 0,
    @SerialName("hd5") val hd5: Int = 0,
    @SerialName("no_reprint") val noReprint: Int = 0,
    @SerialName("autoplay") val autoplay: Int = 0,
    @SerialName("ugc_pay") val ugcPay: Int = 0,
    @SerialName("is_cooperation") val isCooperation: Int = 0,
    @SerialName("ugc_pay_preview") val ugcPayPreview: Int = 0,
    @SerialName("no_background") val noBackground: Int = 0,
    @SerialName("clean_mode") val cleanMode: Int = 0,
    @SerialName("is_stein_gate") val isSteinGate: Int = 0,
    @SerialName("is_360") val is360: Int = 0,
    @SerialName("no_share") val noShare: Int = 0,
    @SerialName("arc_pay") val arcPay: Int = 0,
    @SerialName("free_watch") val freeWatch: Int = 0
)

@Serializable
data class BiliVideoOwner(
    @SerialName("mid") val mid: Long = 0,
    @SerialName("name") val name: String = "",
    @SerialName("face") val face: String = ""
)

@Serializable
data class BiliVideoStat(
    @SerialName("view") val view: Int = 0,
    @SerialName("danmaku") val danmaku: Int = 0,
    @SerialName("reply") val reply: Int = 0,
    @SerialName("favorite") val favorite: Int = 0,
    @SerialName("coin") val coin: Int = 0,
    @SerialName("share") val share: Int = 0,
    @SerialName("now_rank") val nowRank: Int = 0,
    @SerialName("his_rank") val hisRank: Int = 0,
    @SerialName("like") val like: Int = 0,
    @SerialName("dislike") val dislike: Int = 0,
    @SerialName("evaluation") val evaluation: String = "",
)

@Serializable
data class BiliVideoDimension(
    @SerialName("width") val width: Int = 0,
    @SerialName("height") val height: Int = 0,
    @SerialName("rotate") val rotate: Int = 0
)

@Serializable
data class BiliVideoPage(
    @SerialName("cid") val cid: Long = 0,
    @SerialName("page") val page: Int = 0,
    @SerialName("from") val from: String = "",
    @SerialName("part") val part: String = "",
    @SerialName("duration") val duration: Long = 0,
    @SerialName("vid") val vid: String = "",
    @SerialName("weblink") val weblink: String = "",
    @SerialName("dimension") val dimension: BiliVideoDimension = BiliVideoDimension(),
    @SerialName("first_frame") val firstFrame: String = ""
)

@Serializable
data class BiliVideoSubtitle(
    @SerialName("allow_submit") val allowSubmit: Boolean = false,
    @SerialName("list") val list: List<BiliVideoSubtitleItem> = emptyList()
)

@Serializable
data class BiliVideoSubtitleItem(
    @SerialName("id") val id: Long = 0,
    @SerialName("lan") val lan: String = "",
    @SerialName("lan_doc") val lanDoc: String = "",
    @SerialName("is_lock") val isLock: Boolean = false,
    @SerialName("author_mid") val authorMid: Int = 0,
    @SerialName("subtitle_url") val subtitleUrl: String = "",
    @SerialName("author") val author: BiliVideoSubtitleAuthor = BiliVideoSubtitleAuthor()
)

@Serializable
data class BiliVideoSubtitleAuthor(
    @SerialName("mid") val mid: Long = 0,
    @SerialName("name") val name: String = "",
    @SerialName("sex") val sex: String = "",
    @SerialName("face") val face: String = "",
    @SerialName("sign") val sign: String = "",
)

@Serializable
data class BiliVideoStaff(
    @SerialName("mid") val mid: Long = 0,
    @SerialName("title") val title: String = "",
    @SerialName("name") val name: String = "",
    @SerialName("face") val face: String = "",
    @SerialName("vip") val vip: BiliVideoVip,
    @SerialName("official") val official: BiliVideoOfficial = BiliVideoOfficial(),
    @SerialName("follower") val follower: Int = 0
)

@Serializable
data class BiliVideoVip(
    @SerialName("type") val type: Int = 0,
    @SerialName("status") val status: Int = 0,
    @SerialName("theme_type") val themeType: Int = 0
)

@Serializable
data class BiliVideoOfficial(
    @SerialName("role") val role: Int = 0,
    @SerialName("title") val title: String = "",
    @SerialName("desc") val desc: String = "",
    @SerialName("type") val type: Int = 0
)
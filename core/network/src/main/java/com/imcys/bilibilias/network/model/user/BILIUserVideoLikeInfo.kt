package com.imcys.bilibilias.network.model.user


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable





@Serializable
data class BILIUserVideoLikeInfo(
    @SerialName("list")
    val list: List<LikeAndCoinItemData>
)



@Serializable
data class LikeAndCoinItemData(
    @SerialName("aid")
    val aid: Long,
    @SerialName("bvid")
    val bvid: String,
    @SerialName("cid")
    val cid: Long?,
    @SerialName("copyright")
    val copyright: Long,
    @SerialName("cover43")
    val cover43: String?,
    @SerialName("ctime")
    val ctime: Long,
    @SerialName("desc")
    val desc: String,
    @SerialName("duration")
    val duration: Long,
    @SerialName("first_frame")
    val firstFrame: String?,
    @SerialName("owner")
    val owner: Owner,
    @SerialName("pic")
    val pic: String,
    @SerialName("pubdate")
    val pubdate: Long,
    @SerialName("short_link_v2")
    val shortLinkV2: String?,
    @SerialName("stat")
    val stat: Stat,
    @SerialName("subtitle")
    val subtitle: String,
    @SerialName("title")
    val title: String,
    @SerialName("tname")
    val tname: String,
    @SerialName("tnamev2")
    val tnamev2: String,
) {
    @Serializable
    data class Dimension(
        @SerialName("height")
        val height: Long,
        @SerialName("rotate")
        val rotate: Long,
        @SerialName("width")
        val width: Long
    )

    @Serializable
    data class Owner(
        @SerialName("face")
        val face: String,
        @SerialName("mid")
        val mid: Long,
        @SerialName("name")
        val name: String
    )

    @Serializable
    data class Rights(
        @SerialName("arc_pay")
        val arcPay: Long,
        @SerialName("autoplay")
        val autoplay: Long,
        @SerialName("bp")
        val bp: Long,
        @SerialName("download")
        val download: Long,
        @SerialName("elec")
        val elec: Long,
        @SerialName("hd5")
        val hd5: Long,
        @SerialName("is_cooperation")
        val isCooperation: Long,
        @SerialName("movie")
        val movie: Long,
        @SerialName("no_background")
        val noBackground: Long,
        @SerialName("no_reprLong")
        val noReprLong: Long,
        @SerialName("pay")
        val pay: Long,
        @SerialName("pay_free_watch")
        val payFreeWatch: Long,
        @SerialName("ugc_pay")
        val ugcPay: Long,
        @SerialName("ugc_pay_preview")
        val ugcPayPreview: Long
    )

    @Serializable
    data class Stat(
        @SerialName("aid")
        val aid: Long,
        @SerialName("coin")
        val coin: Long,
        @SerialName("danmaku")
        val danmaku: Long,
        @SerialName("dislike")
        val dislike: Long,
        @SerialName("fav_g")
        val favG: Long,
        @SerialName("favorite")
        val favorite: Long,
        @SerialName("his_rank")
        val hisRank: Long,
        @SerialName("like")
        val like: Long,
        @SerialName("like_g")
        val likeG: Long,
        @SerialName("now_rank")
        val nowRank: Long,
        @SerialName("reply")
        val reply: Long,
        @SerialName("share")
        val share: Long,
        @SerialName("view")
        val view: Long,
        @SerialName("vt")
        val vt: Long,
        @SerialName("vv")
        val vv: Long
    )
}
package com.imcys.bilibilias.core.model.video

import com.imcys.bilibilias.core.model.user.Official
import com.imcys.bilibilias.core.model.user.Vip
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ViewDetail(
    @SerialName("aid")
    val aid: Long = 0,
    @SerialName("argue_info")
    val argueInfo: ArgueInfo = ArgueInfo(),
    @SerialName("bvid")
    val bvid: String = "",
    @SerialName("cid")
    val cid: Long = 0,
    @SerialName("copyright")
    val copyright: Int = 0,
    @SerialName("ctime")
    val ctime: Int = 0,
    @SerialName("desc")
    val desc: String = "",
    @SerialName("desc_v2")
    val descV2: List<DescV2> = listOf(),
    @SerialName("dimension")
    val dimension: Dimension = Dimension(),
    @SerialName("disable_show_up_info")
    val disableShowUpInfo: Boolean = false,
    @SerialName("duration")
    val duration: Int = 0,
    @SerialName("dynamic")
    val `dynamic`: String = "",
    @SerialName("enable_vt")
    val enableVt: Int = 0,
    @SerialName("honor_reply")
    val honorReply: HonorReply = HonorReply(),
    @SerialName("is_chargeable_season")
    val isChargeableSeason: Boolean = false,
    @SerialName("is_season_display")
    val isSeasonDisplay: Boolean = false,
    @SerialName("is_story")
    val isStory: Boolean = false,
    @SerialName("is_story_play")
    val isStoryPlay: Int = 0,
    @SerialName("is_upower_exclusive")
    val isUpowerExclusive: Boolean = false,
    @SerialName("is_upower_play")
    val isUpowerPlay: Boolean = false,
    @SerialName("is_upower_preview")
    val isUpowerPreview: Boolean = false,
    @SerialName("like_icon")
    val likeIcon: String = "",
    @SerialName("mission_id")
    val missionId: Int = 0,
    @SerialName("need_jump_bv")
    val needJumpBv: Boolean = false,
    @SerialName("no_cache")
    val noCache: Boolean = false,
    @SerialName("owner")
    val owner: Owner = Owner(),
    @SerialName("pages")
    val pages: List<Pages> = listOf(),
    @SerialName("pic")
    val pic: String = "",
    @SerialName("pubdate")
    val pubdate: Int = 0,
    @SerialName("rights")
    val rights: Rights = Rights(),
    @SerialName("staff")
    val staff: List<Staff> = listOf(),
    @SerialName("stat")
    val stat: Stat = Stat(),
    @SerialName("state")
    val state: Int = 0,
    @SerialName("subtitle")
    val subtitle: Subtitle = Subtitle(),
    @SerialName("teenage_mode")
    val teenageMode: Int = 0,
    @SerialName("tid")
    val tid: Int = 0,
    @SerialName("title")
    val title: String = "",
    @SerialName("tname")
    val tname: String = "",
    @SerialName("user_garb")
    val userGarb: UserGarb = UserGarb(),
    @SerialName("videos")
    val videos: Int = 0,
    @SerialName("vt_display")
    val vtDisplay: String = "",
    @SerialName("redirect_url")
    // https://www.bilibili.com/bangumi/play/ep809844
    val redirectUrl: String? = null
) {
    @Serializable
    data class ArgueInfo(
        @SerialName("argue_link")
        val argueLink: String = "",
        @SerialName("argue_msg")
        val argueMsg: String = "",
        @SerialName("argue_type")
        val argueType: Int = 0
    )

    @Serializable
    data class DescV2(
        @SerialName("biz_id")
        val bizId: Int = 0,
        @SerialName("raw_text")
        val rawText: String = "",
        @SerialName("type")
        val type: Int = 0
    )

    @Serializable
    data class Pages(
        @SerialName("cid")
        val cid: Long = 0,
        @SerialName("dimension")
        val dimension: Dimension = Dimension(),
        @SerialName("duration")
        val duration: Int = 0,
        @SerialName("from")
        val from: String = "",
        @SerialName("page")
        val page: Int = 0,
        @SerialName("part")
        val part: String = "",
        @SerialName("vid")
        val vid: String = "",
        @SerialName("weblink")
        val weblink: String = ""
    )


    @Serializable
    data class Staff(
        @SerialName("face")
        val face: String = "",
        @SerialName("follower")
        val follower: Int = 0,
        @SerialName("label_style")
        val labelStyle: Int = 0,
        @SerialName("mid")
        val mid: Long = 0,
        @SerialName("name")
        val name: String = "",
        @SerialName("official")
        val official: Official = Official(),
        @SerialName("title")
        val title: String = "",
        @SerialName("vip")
        val vip: Vip = Vip()
    ) {

    }

    @Serializable
    data class UserGarb(
        @SerialName("url_image_ani_cut")
        val urlImageAniCut: String = ""
    )
}

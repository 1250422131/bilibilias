package com.imcys.bilibilias.network.model.video

import com.imcys.bilibilias.network.model.video.BILIVideoViewInfo.UgcSeason.Section.Episode
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class BILIVideoViewInfo(
    @SerialName("aid")
    val aid: Long,
    @SerialName("argue_info")
    val argueInfo: ArgueInfo,
    @SerialName("bvid")
    val bvid: String,
    @SerialName("cid")
    val cid: Long,
    @SerialName("copyright")
    val copyright: Long,
    @SerialName("ctime")
    val ctime: Long?,
    @SerialName("desc")
    val desc: String,
    @SerialName("dimension")
    val dimension: Dimension,
    @SerialName("disable_show_up_info")
    val disableShowUpInfo: Boolean,
    @SerialName("duration")
    val duration: Long,
    @SerialName("dynamic")
    val `dynamic`: String,
    @SerialName("enable_vt")
    val enableVt: Long,
    @SerialName("honor_reply")
    val honorReply: HonorReply?,
    @SerialName("is_chargeable_season")
    val isChargeableSeason: Boolean,
    @SerialName("is_season_display")
    val isSeasonDisplay: Boolean,
    @SerialName("is_story")
    val isStory: Boolean,
    @SerialName("is_story_play")
    val isStoryPlay: Long,
    @SerialName("is_upower_exclusive")
    val isUpowerExclusive: Boolean,
    @SerialName("is_upower_exclusive_with_qa")
    val isUpowerExclusiveWithQa: Boolean,
    @SerialName("is_upower_play")
    val isUpowerPlay: Boolean,
    @SerialName("is_upower_preview")
    val isUpowerPreview: Boolean,
    @SerialName("is_view_self")
    val isViewSelf: Boolean,
    @SerialName("like_icon")
    val likeIcon: String,
    @SerialName("mission_id")
    val missionId: Long = 0,
    @SerialName("need_jump_bv")
    val needJumpBv: Boolean,
    @SerialName("no_cache")
    val noCache: Boolean,
    @SerialName("owner")
    val owner: Owner,
    @SerialName("pages")
    val pages: List<Page>?,
    @SerialName("pic")
    val pic: String,
    @SerialName("pubdate")
    val pubdate: Long,
    @SerialName("rights")
    val rights: Rights?,
    @SerialName("season_id")
    val seasonId: Long = 0,
    @SerialName("stat")
    val stat: Stat?,
    @SerialName("state")
    val state: Long,
    @SerialName("subtitle")
    val subtitle: Subtitle?,
    @SerialName("teenage_mode")
    val teenageMode: Long,
    @SerialName("tid")
    val tid: Long,
    @SerialName("tid_v2")
    val tidV2: Long,
    @SerialName("title")
    val title: String,
    @SerialName("tname")
    val tname: String,
    @SerialName("tname_v2")
    val tnameV2: String,
    @SerialName("ugc_season")
    val ugcSeason: UgcSeason?,
    @SerialName("user_garb")
    val userGarb: UserGarb?,
    @SerialName("videos")
    val videos: Long,
    @SerialName("vt_display")
    val vtDisplay: String,
    @SerialName("redirect_url")
    val redirectUrl: String? = null
) {
    @Serializable
    data class ArgueInfo(
        @SerialName("argue_link")
        val argueLink: String,
        @SerialName("argue_msg")
        val argueMsg: String,
        @SerialName("argue_type")
        val argueType: Long
    )

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
    class HonorReply

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
    data class Page(
        @SerialName("cid")
        val cid: Long,
        @SerialName("ctime")
        val ctime: Long?,
        @SerialName("dimension")
        val dimension: Dimension,
        @SerialName("duration")
        val duration: Long,
        @SerialName("first_frame")
        val firstFrame: String?,
        @SerialName("from")
        val from: String,
        @SerialName("page")
        val page: Long,
        @SerialName("part")
        val part: String,
        @SerialName("vid")
        val vid: String,
        @SerialName("weblink")
        val weblink: String
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
    }

    @Serializable
    data class Rights(
        @SerialName("arc_pay")
        val arcPay: Long? = 0,
        @SerialName("autoplay")
        val autoplay: Long? = 0,
        @SerialName("bp")
        val bp: Long? = 0,
        @SerialName("clean_mode")
        val cleanMode: Long? = 0,
        @SerialName("download")
        val download: Long? = 0,
        @SerialName("elec")
        val elec: Long? = 0,
        @SerialName("free_watch")
        val freeWatch: Long,
        @SerialName("hd5")
        val hd5: Long? = 0,
        @SerialName("is_360")
        val is360: Long? = 0,
        @SerialName("is_cooperation")
        val isCooperation: Long? = 0,
        @SerialName("is_stein_gate")
        val isSteinGate: Long? = 0,
        @SerialName("movie")
        val movie: Long? = 0,
        @SerialName("no_background")
        val noBackground: Long? = 0,
        @SerialName("no_reprLong")
        val noReprLong: Long? = 0,
        @SerialName("no_share")
        val noShare: Long? = 0,
        @SerialName("pay")
        val pay: Long? = 0,
        @SerialName("ugc_pay")
        val ugcPay: Long? = 0,
        @SerialName("ugc_pay_preview")
        val ugcPayPreview: Long? = 0,
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
        @SerialName("evaluation")
        val evaluation: String,
        @SerialName("favorite")
        val favorite: Long,
        @SerialName("his_rank")
        val hisRank: Long,
        @SerialName("like")
        val like: Long,
        @SerialName("now_rank")
        val nowRank: Long,
        @SerialName("reply")
        val reply: Long,
        @SerialName("share")
        val share: Long,
        @SerialName("view")
        val view: Long,
        @SerialName("vt")
        val vt: Long
    )

    @Serializable
    data class UgcSeason(
        @SerialName("attribute")
        val attribute: Long,
        @SerialName("cover")
        val cover: String,
        @SerialName("enable_vt")
        val enableVt: Long,
        @SerialName("ep_count")
        val epCount: Long,
        @SerialName("id")
        val id: Long,
        @SerialName("is_pay_season")
        val isPaySeason: Boolean,
        @SerialName("mid")
        val mid: Long,
        @SerialName("season_type")
        val seasonType: Long,
        @SerialName("sections")
        val sections: List<Section>,
        @SerialName("sign_state")
        val signState: Long,
        @SerialName("stat")
        val stat: Stat,
        @SerialName("title")
        val title: String
    ) {
        @Serializable
        data class Section(
            @SerialName("episodes")
            val episodes: List<Episode>,
            @SerialName("id")
            val id: Long,
            @SerialName("season_id")
            val seasonId: Long? = 0,
            @SerialName("title")
            val title: String,
            @SerialName("type")
            val type: Long
        ) {
            @Serializable
            data class Episode(
                @SerialName("aid")
                val aid: Long,
                @SerialName("arc")
                val arc: Arc,
                @SerialName("attribute")
                val attribute: Long,
                @SerialName("bvid")
                val bvid: String,
                @SerialName("cid")
                val cid: Long,
                @SerialName("id")
                val id: Long,
                @SerialName("page")
                val page: Page?,
                @SerialName("pages")
                val pages: List<Page>,
                @SerialName("season_id")
                val seasonId: Long? = 0,
                @SerialName("section_id")
                val sectionId: Long? = 0,
                @SerialName("title")
                val title: String
            ) {
                @Serializable
                data class Arc(
                    @SerialName("aid")
                    val aid: Long,
                    @SerialName("author")
                    val author: Subtitle.Item.Author?,
                    @SerialName("copyright")
                    val copyright: Long,
                    @SerialName("ctime")
                    val ctime: Long?,
                    @SerialName("desc")
                    val desc: String,
                    @SerialName("dimension")
                    val dimension: Dimension,
                    @SerialName("duration")
                    val duration: Long,
                    @SerialName("dynamic")
                    val `dynamic`: String,
                    @SerialName("enable_vt")
                    val enableVt: Long,
                    @SerialName("is_blooper")
                    val isBlooper: Boolean,
                    @SerialName("is_chargeable_season")
                    val isChargeableSeason: Boolean,
                    @SerialName("is_lesson_video")
                    val isLessonVideo: Long,
                    @SerialName("pic")
                    val pic: String,
                    @SerialName("pubdate")
                    val pubdate: Long,
                    @SerialName("rights")
                    val rights: Rights?,
                    @SerialName("stat")
                    val stat: Stat,
                    @SerialName("state")
                    val state: Long,
                    @SerialName("title")
                    val title: String,
                    @SerialName("type_id")
                    val typeId: Long,
                    @SerialName("type_id_v2")
                    val typeIdV2: Long,
                    @SerialName("type_name")
                    val typeName: String,
                    @SerialName("type_name_v2")
                    val typeNameV2: String,
                    @SerialName("videos")
                    val videos: Long,
                    @SerialName("vt_display")
                    val vtDisplay: String
                )
            }
        }

        @Serializable
        data class Stat(
            @SerialName("coin")
            val coin: Long,
            @SerialName("danmaku")
            val danmaku: Long,
            @SerialName("fav")
            val fav: Long,
            @SerialName("his_rank")
            val hisRank: Long,
            @SerialName("like")
            val like: Long,
            @SerialName("now_rank")
            val nowRank: Long,
            @SerialName("reply")
            val reply: Long,
            @SerialName("season_id")
            val seasonId: Long? = 0,
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

    @Serializable
    data class UserGarb(
        @SerialName("url_image_ani_cut")
        val urlImageAniCut: String
    )
}


@Serializable
data class Subtitle(
    @SerialName("allow_submit")
    val allowSubmit: Boolean,
    @SerialName("list")
    val list: List<Item> = emptyList(),
    @SerialName("subtitles")
    val subtitles: List<Item> = emptyList()
) {
    @Serializable
    data class Item(
        @SerialName("ai_status")
        val aiStatus: Long,
        @SerialName("ai_type")
        val aiType: Long,
        @SerialName("author")
        val author: Author?,
        @SerialName("id")
        val id: Long,
        @SerialName("id_str")
        val idStr: String,
        @SerialName("is_lock")
        val isLock: Boolean,
        @SerialName("lan")
        val lan: String,
        @SerialName("lan_doc")
        val lanDoc: String,
        @SerialName("subtitle_url")
        val subtitleUrl: String,
        @SerialName("subtitle_url_v2")
        val subtitleUrlV2: String?,
        @SerialName("type")
        val type: Long
    ) {
        @Serializable
        data class Author(
            @SerialName("birthday")
            val birthday: Long?,
            @SerialName("face")
            val face: String,
            @SerialName("in_reg_audit")
            val inRegAudit: Long?,
            @SerialName("is_deleted")
            val isDeleted: Long?,
            @SerialName("is_fake_account")
            val isFakeAccount: Long?,
            @SerialName("is_senior_member")
            val isSeniorMember: Long?,
            @SerialName("mid")
            val mid: Long,
            @SerialName("name")
            val name: String,
            @SerialName("rank")
            val rank: Long?,
            @SerialName("sex")
            val sex: String?,
            @SerialName("sign")
            val sign: String?
        )
    }
}

fun List<Episode>.filterWithMultiplePages(): List<Episode> {
    return filter { it.pages.size > 1 }
}

fun List<Episode>.filterWithSinglePage(): List<Episode> {
    return filter { it.pages.size <= 1 }
}
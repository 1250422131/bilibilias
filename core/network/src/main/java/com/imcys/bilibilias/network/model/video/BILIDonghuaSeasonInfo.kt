package com.imcys.bilibilias.network.model.video

import kotlinx.serialization.Serializable

import kotlinx.serialization.SerialName

/**
 * Anime
 * 番剧/动画
 * 为了更加全面，我们这里叫Donghua->动画
 */
@Serializable
data class BILIDonghuaSeasonInfo(
    @SerialName("activity")
    val activity: Activity,
    @SerialName("actors")
    val actors: String,
    @SerialName("alias")
    val alias: String,
    @SerialName("areas")
    val areas: List<Area>,
    @SerialName("bkg_cover")
    val bkgCover: String,
    @SerialName("cover")
    val cover: String,
    @SerialName("delivery_fragment_video")
    val deliveryFragmentVideo: Boolean,
    @SerialName("enable_vt")
    val enableVt: Boolean,
    @SerialName("episodes")
    val episodes: List<Episode>,
    @SerialName("evaluate")
    val evaluate: String,
    @SerialName("freya")
    val freya: Freya?,
    @SerialName("hide_ep_vv_vt_dm")
    val hideEpVvVtDm: Long,
    @SerialName("icon_font")
    val iconFont: IconFont?,
    @SerialName("jp_title")
    val jpTitle: String,
    @SerialName("link")
    val link: String,
    @SerialName("media_id")
    val mediaId: Long,
    @SerialName("mode")
    val mode: Long,
    @SerialName("new_ep")
    val newEp: NewEp?,
    @SerialName("payment")
    val payment: Payment?,
    @SerialName("play_strategy")
    val playStrategy: PlayStrategy?,
    @SerialName("positive")
    val positive: Positive?,
    @SerialName("publish")
    val publish: Publish,
    @SerialName("rating")
    val rating: Rating,
    @SerialName("record")
    val record: String,
    @SerialName("rights")
    val rights: Rights?,
    @SerialName("season_id")
    val seasonId: Long,
    @SerialName("season_title")
    val seasonTitle: String,
    @SerialName("seasons")
    val seasons: List<Season> = emptyList(),
    @SerialName("section")
    val section: List<Section> = emptyList(),
    @SerialName("series")
    val series: Series,
    @SerialName("share_copy")
    val shareCopy: String,
    @SerialName("share_sub_title")
    val shareSubTitle: String,
    @SerialName("share_url")
    val shareUrl: String,
    @SerialName("show")
    val show: Show,
    @SerialName("show_season_type")
    val showSeasonType: Long,
    @SerialName("square_cover")
    val squareCover: String,
    @SerialName("staff")
    val staff: String,
    @SerialName("stat")
    val stat: Stat,
    @SerialName("status")
    val status: Long,
    @SerialName("styles")
    val styles: List<String>,
    @SerialName("subtitle")
    val subtitle: String,
    @SerialName("title")
    val title: String,
    @SerialName("total")
    val total: Long,
    @SerialName("type")
    val type: Long,
    @SerialName("up_info")
    val upInfo: UpInfo?,
    @SerialName("user_status")
    val userStatus: UserStatus?,
) {
    @Serializable
    data class Activity(
        @SerialName("head_bg_url")
        val headBgUrl: String,
        @SerialName("id")
        val id: Long,
        @SerialName("title")
        val title: String
    )

    @Serializable
    data class Area(
        @SerialName("id")
        val id: Long,
        @SerialName("name")
        val name: String
    )

    @Serializable
    data class Episode(
        @SerialName("aid")
        val aid: Long,
        @SerialName("badge")
        val badge: String,
        @SerialName("badge_info")
        val badgeInfo: BadgeInfo?,
        @SerialName("badge_type")
        val badgeType: Long = -1,
        @SerialName("bvid")
        val bvid: String?,
        @SerialName("cid")
        val cid: Long,
        @SerialName("cover")
        val cover: String,
        @SerialName("dimension")
        val dimension: Dimension?,
        @SerialName("duration")
        val duration: Long = 0,
        @SerialName("enable_vt")
        val enableVt: Boolean,
        @SerialName("ep_id")
        val epId: Long,
        @SerialName("from")
        val from: String?,
        @SerialName("id")
        val id: Long,
        @SerialName("is_view_hide")
        val isViewHide: Boolean,
        @SerialName("link")
        val link: String,
        @SerialName("long_title")
        val longTitle: String = "",
        @SerialName("pub_time")
        val pubTime: Long,
        @SerialName("pv")
        val pv: Long,
        @SerialName("release_date")
        val releaseDate: String?,
        @SerialName("rights")
        val rights: Rights?,
        @SerialName("section_type")
        val sectionType: Long,
        @SerialName("share_copy")
        val shareCopy: String?,
        @SerialName("share_url")
        val shareUrl: String?,
        @SerialName("short_link")
        val shortLink: String?,
        @SerialName("showDrmLoginDialog")
        val showDrmLoginDialog: Boolean,
        @SerialName("show_title")
        val showTitle: String?,
        @SerialName("skip")
        val skip: Skip?,
        @SerialName("status")
        val status: Long,
        @SerialName("subtitle")
        val subtitle: String?,
        @SerialName("title")
        val title: String,
        @SerialName("vid")
        val vid: String?
    ) {
        @Serializable
        data class BadgeInfo(
            @SerialName("bg_color")
            val bgColor: String,
            @SerialName("bg_color_night")
            val bgColorNight: String,
            @SerialName("text")
            val text: String
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
        data class Rights(
            @SerialName("allow_dm")
            val allowDm: Long,
            @SerialName("allow_download")
            val allowDownload: Long,
            @SerialName("area_limit")
            val areaLimit: Long
        )

        @Serializable
        data class Skip(
            @SerialName("ed")
            val ed: Ed?,
            @SerialName("op")
            val op: Op?
        ) {
            @Serializable
            data class Ed(
                @SerialName("end")
                val end: Long,
                @SerialName("start")
                val start: Long
            )

            @Serializable
            data class Op(
                @SerialName("end")
                val end: Long,
                @SerialName("start")
                val start: Long
            )
        }
    }

    @Serializable
    data class Freya(
        @SerialName("bubble_desc")
        val bubbleDesc: String?,
        @SerialName("bubble_show_cnt")
        val bubbleShowCnt: Long = 0,
        @SerialName("icon_show")
        val iconShow: Long = 0
    )

    @Serializable
    data class IconFont(
        @SerialName("name")
        val name: String,
        @SerialName("text")
        val text: String
    )

    @Serializable
    data class NewEp(
        @SerialName("desc")
        val desc: String,
        @SerialName("id")
        val id: Long,
        @SerialName("is_new")
        val isNew: Long,
        @SerialName("title")
        val title: String
    )

    @Serializable
    data class Payment(
        @SerialName("discount")
        val discount: Long,
        @SerialName("pay_type")
        val payType: PayType,
        @SerialName("price")
        val price: String,
        @SerialName("promotion")
        val promotion: String,
        @SerialName("tip")
        val tip: String,
        @SerialName("view_start_time")
        val viewStartTime: Long,
        @SerialName("vip_discount")
        val vipDiscount: Long,
        @SerialName("vip_first_promotion")
        val vipFirstPromotion: String,
        @SerialName("vip_price")
        val vipPrice: String,
        @SerialName("vip_promotion")
        val vipPromotion: String
    ) {
        @Serializable
        data class PayType(
            @SerialName("allow_discount")
            val allowDiscount: Long,
            @SerialName("allow_pack")
            val allowPack: Long,
            @SerialName("allow_ticket")
            val allowTicket: Long,
            @SerialName("allow_time_limit")
            val allowTimeLimit: Long,
            @SerialName("allow_vip_discount")
            val allowVipDiscount: Long,
            @SerialName("forbid_bb")
            val forbidBb: Long
        )
    }

    @Serializable
    data class PlayStrategy(
        @SerialName("strategies")
        val strategies: List<String>
    )

    @Serializable
    data class Positive(
        @SerialName("id")
        val id: Long,
        @SerialName("title")
        val title: String
    )

    @Serializable
    data class Publish(
        @SerialName("is_finish")
        val isFinish: Long,
        @SerialName("is_started")
        val isStarted: Long,
        @SerialName("pub_time")
        val pubTime: String,
        @SerialName("pub_time_show")
        val pubTimeShow: String,
        @SerialName("unknow_pub_date")
        val unknowPubDate: Long,
        @SerialName("weekday")
        val weekday: Long
    )

    @Serializable
    data class Rating(
        @SerialName("count")
        val count: Long,
        @SerialName("score")
        val score: Double
    )

    @Serializable
    data class Rights(
        @SerialName("allow_bp")
        val allowBp: Long,
        @SerialName("allow_bp_rank")
        val allowBpRank: Long,
        @SerialName("allow_download")
        val allowDownload: Long,
        @SerialName("allow_review")
        val allowReview: Long,
        @SerialName("area_limit")
        val areaLimit: Long,
        @SerialName("ban_area_show")
        val banAreaShow: Long,
        @SerialName("can_watch")
        val canWatch: Long,
        @SerialName("copyright")
        val copyright: String,
        @SerialName("forbid_pre")
        val forbidPre: Long,
        @SerialName("freya_white")
        val freyaWhite: Long,
        @SerialName("is_cover_show")
        val isCoverShow: Long,
        @SerialName("is_preview")
        val isPreview: Long,
        @SerialName("is_sponsor")
        val isSponsor: Long,
        @SerialName("only_vip_download")
        val onlyVipDownload: Long,
        @SerialName("resource")
        val resource: String,
        @SerialName("watch_platform")
        val watchPlatform: Long
    )

    @Serializable
    data class Season(
        @SerialName("badge")
        val badge: String,
        @SerialName("badge_info")
        val badgeInfo: BadgeInfo,
        @SerialName("badge_type")
        val badgeType: Long,
        @SerialName("cover")
        val cover: String,
        @SerialName("enable_vt")
        val enableVt: Boolean,
        @SerialName("horizontal_cover_1610")
        val horizontalCover1610: String,
        @SerialName("horizontal_cover_169")
        val horizontalCover169: String,
        @SerialName("icon_font")
        val iconFont: IconFont,
        @SerialName("media_id")
        val mediaId: Long,
        @SerialName("new_ep")
        val newEp: NewEp,
        @SerialName("season_id")
        val seasonId: Long,
        @SerialName("season_title")
        val seasonTitle: String,
        @SerialName("season_type")
        val seasonType: Long,
        @SerialName("stat")
        val stat: Stat?
    ) {
        @Serializable
        data class BadgeInfo(
            @SerialName("bg_color")
            val bgColor: String,
            @SerialName("bg_color_night")
            val bgColorNight: String,
            @SerialName("text")
            val text: String
        )

        @Serializable
        data class IconFont(
            @SerialName("name")
            val name: String,
            @SerialName("text")
            val text: String
        )

        @Serializable
        data class NewEp(
            @SerialName("cover")
            val cover: String,
            @SerialName("id")
            val id: Long,
            @SerialName("index_show")
            val indexShow: String
        )

        @Serializable
        data class Stat(
            @SerialName("favorites")
            val favorites: Long,
            @SerialName("series_follow")
            val seriesFollow: Long,
            @SerialName("views")
            val views: Long,
            @SerialName("vt")
            val vt: Long
        )
    }

    @Serializable
    data class Section(
        @SerialName("attr")
        val attr: Long,
        @SerialName("episode_id")
        val episodeId: Long,
        @SerialName("episodes")
        val episodes: List<Episode>,
        @SerialName("id")
        val id: Long,
        @SerialName("title")
        val title: String,
        @SerialName("type")
        val type: Long,
        @SerialName("type2")
        val type2: Long
    )

    @Serializable
    data class Series(
        @SerialName("display_type")
        val displayType: Long,
        @SerialName("series_id")
        val seriesId: Long,
        @SerialName("series_title")
        val seriesTitle: String
    )

    @Serializable
    data class Show(
        @SerialName("wide_screen")
        val wideScreen: Long
    )

    @Serializable
    data class Stat(
        @SerialName("coins")
        val coins: Long,
        @SerialName("danmakus")
        val danmakus: Long,
        @SerialName("favorite")
        val favorite: Long,
        @SerialName("favorites")
        val favorites: Long,
        @SerialName("follow_text")
        val followText: String,
        @SerialName("likes")
        val likes: Long,
        @SerialName("reply")
        val reply: Long,
        @SerialName("share")
        val share: Long,
        @SerialName("views")
        val views: Long,
        @SerialName("vt")
        val vt: Long
    )

    @Serializable
    data class UpInfo(
        @SerialName("avatar")
        val avatar: String,
        @SerialName("avatar_subscript_url")
        val avatarSubscriptUrl: String,
        @SerialName("follower")
        val follower: Long,
        @SerialName("is_follow")
        val isFollow: Long,
        @SerialName("mid")
        val mid: Long,
        @SerialName("nickname_color")
        val nicknameColor: String,
        @SerialName("pendant")
        val pendant: Pendant,
        @SerialName("theme_type")
        val themeType: Long,
        @SerialName("uname")
        val uname: String,
        @SerialName("verify_type")
        val verifyType: Long,
        @SerialName("vip_label")
        val vipLabel: VipLabel,
        @SerialName("vip_status")
        val vipStatus: Long,
        @SerialName("vip_type")
        val vipType: Long
    ) {
        @Serializable
        data class Pendant(
            @SerialName("image")
            val image: String,
            @SerialName("name")
            val name: String,
            @SerialName("pid")
            val pid: Long
        )

        @Serializable
        data class VipLabel(
            @SerialName("bg_color")
            val bgColor: String,
            @SerialName("bg_style")
            val bgStyle: Long,
            @SerialName("border_color")
            val borderColor: String,
            @SerialName("text")
            val text: String,
            @SerialName("text_color")
            val textColor: String
        )
    }

    @Serializable
    data class UserStatus(
        @SerialName("area_limit")
        val areaLimit: Long,
        @SerialName("ban_area_show")
        val banAreaShow: Long,
        @SerialName("follow")
        val follow: Long,
        @SerialName("follow_status")
        val followStatus: Long,
        @SerialName("login")
        val login: Long,
        @SerialName("pay")
        val pay: Long,
        @SerialName("pay_pack_paid")
        val payPackPaid: Long,
        @SerialName("sponsor")
        val sponsor: Long
    )
}



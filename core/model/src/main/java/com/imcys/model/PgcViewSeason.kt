package com.imcys.model

import com.imcys.model.video.Rights
import com.imcys.model.video.Stat
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * 番剧剧集明细
 *
 * ![获取剧集明细（web端）（ssid/epid方式）](https://github.com/SocialSisterYi/bilibili-API-collect/blob/master/docs/bangumi/info.md#%E8%8E%B7%E5%8F%96%E5%89%A7%E9%9B%86%E6%98%8E%E7%BB%86web%E7%AB%AFssidepid%E6%96%B9%E5%BC%8F)
 */

@Serializable
data class PgcViewSeason(
    @SerialName("actors")
    val actors: String = "",
    @SerialName("alias")
    val alias: String = "",
    @SerialName("areas")
    val areas: List<Area> = listOf(),
    @SerialName("bkg_cover")
    val bkgCover: String = "",
    @SerialName("cover")
    val cover: String = "",
    @SerialName("delivery_fragment_video")
    val deliveryFragmentVideo: Boolean = false,
    @SerialName("episodes")
    val episodes: List<Episode> = listOf(),
    @SerialName("evaluate")
    val evaluate: String = "",
    @SerialName("hide_ep_vv_vt_dm")
    val hideEpVvVtDm: Int = 0,
    @SerialName("link")
    val link: String = "",
    @SerialName("media_id")
    val mediaId: Long = 0,
    @SerialName("new_ep")
    val newEp: NewEp = NewEp(),
    @SerialName("publish")
    val publish: Publish = Publish(),
    @SerialName("rating")
    val rating: Rating = Rating(),
    @SerialName("record")
    val record: String = "",
    @SerialName("rights")
    val rights: Rights = Rights(),
    @SerialName("season_id")
    val seasonId: Long = 0,
    @SerialName("season_title")
    val seasonTitle: String = "",
    @SerialName("seasons")
    val seasons: List<Season> = listOf(),
    @SerialName("section")
    val section: List<Section> = listOf(),
    @SerialName("series")
    val series: Series = Series(),
    @SerialName("share_copy")
    val shareCopy: String = "",
    @SerialName("share_sub_title")
    val shareSubTitle: String = "",
    @SerialName("share_url")
    val shareUrl: String = "",
    @SerialName("show_season_type")
    val showSeasonType: Int = 0,
    @SerialName("square_cover")
    val squareCover: String = "",
    @SerialName("staff")
    val staff: String = "",
    @SerialName("stat")
    val stat: Stat = Stat(),
    @SerialName("subtitle")
    val subtitle: String = "",
    @SerialName("title")
    val title: String = "",
    @SerialName("total")
    val total: Int = 0,
    @SerialName("type")
    val type: Int = 0,
    @SerialName("up_info")
    val upInfo: UpInfo = UpInfo(),
    @SerialName("user_status")
    val userStatus: UserStatus = UserStatus()
) {

    @Serializable
    data class Area(
        @SerialName("id")
        val id: Int = 0,
        @SerialName("name")
        val name: String = ""
    )

    @Serializable
    data class Episode(
        @SerialName("aid")
        val aid: Long = 0,
        @SerialName("bvid")
        val bvid: String = "",
        @SerialName("cid")
        val cid: Long = 0,
        @SerialName("cover")
        val cover: String = "",
        @SerialName("dimension")
        val dimension: Dimension = Dimension(),
        @SerialName("duration")
        val duration: Int = 0,
        @SerialName("ep_id")
        val epId: Long = 0,
        @SerialName("from")
        val from: String = "",
        @SerialName("id")
        val id: Long = 0,
        @SerialName("link")
        val link: String = "",
        @SerialName("long_title")
        val longTitle: String = "",
        @SerialName("pub_time")
        val pubTime: Int = 0,
        @SerialName("pv")
        val pv: Int = 0,
        @SerialName("release_date")
        val releaseDate: String = "",
        @SerialName("rights")
        val rights: Rights = Rights(),
        @SerialName("share_copy")
        val shareCopy: String = "",
        @SerialName("share_url")
        val shareUrl: String = "",
        @SerialName("short_link")
        val shortLink: String = "",
        @SerialName("showDrmLoginDialog")
        val showDrmLoginDialog: Boolean = false,
        @SerialName("subtitle")
        val subtitle: String = "",
        @SerialName("title")
        val title: String = "",
    )

    @Serializable
    data class NewEp(
        @SerialName("desc")
        val desc: String = "",
        @SerialName("id")
        val id: Long = 0,
        @SerialName("is_new")
        val isNew: Int = 0,
        @SerialName("title")
        val title: String = ""
    )

    @Serializable
    data class Payment(
        @SerialName("discount")
        val discount: Int = 0,
        @SerialName("pay_type")
        val payType: PayType = PayType(),
        @SerialName("price")
        val price: String = "",
        @SerialName("promotion")
        val promotion: String = "",
        @SerialName("tip")
        val tip: String = "",
        @SerialName("view_start_time")
        val viewStartTime: Int = 0,
        @SerialName("vip_discount")
        val vipDiscount: Int = 0,
        @SerialName("vip_first_promotion")
        val vipFirstPromotion: String = "",
        @SerialName("vip_price")
        val vipPrice: String = "",
        @SerialName("vip_promotion")
        val vipPromotion: String = ""
    ) {
        @Serializable
        data class PayType(
            @SerialName("allow_discount")
            val allowDiscount: Int = 0,
            @SerialName("allow_pack")
            val allowPack: Int = 0,
            @SerialName("allow_ticket")
            val allowTicket: Int = 0,
            @SerialName("allow_time_limit")
            val allowTimeLimit: Int = 0,
            @SerialName("allow_vip_discount")
            val allowVipDiscount: Int = 0,
            @SerialName("forbid_bb")
            val forbidBb: Int = 0
        )
    }

    @Serializable
    data class PlayStrategy(
        @SerialName("strategies")
        val strategies: List<String> = listOf()
    )

    @Serializable
    data class Publish(
        @SerialName("is_finish")
        val isFinish: Int = 0,
        @SerialName("is_started")
        val isStarted: Int = 0,
        @SerialName("pub_time")
        val pubTime: String = "",
        @SerialName("pub_time_show")
        val pubTimeShow: String = "",
        @SerialName("unknow_pub_date")
        val unknowPubDate: Int = 0,
        @SerialName("weekday")
        val weekday: Int = 0
    )

    @Serializable
    data class Rating(
        @SerialName("count")
        val count: Int = 0,
        @SerialName("score")
        val score: Double = 0.0
    )

    @Serializable
    data class Season(
        @SerialName("cover")
        val cover: String = "",
        @SerialName("horizontal_cover_1610")
        val horizontalCover1610: String = "",
        @SerialName("horizontal_cover_169")
        val horizontalCover169: String = "",
        @SerialName("media_id")
        val mediaId: Long = 0,
        @SerialName("new_ep")
        val newEp: NewEp = NewEp(),
        @SerialName("season_id")
        val seasonId: Int = 0,
        @SerialName("season_title")
        val seasonTitle: String = "",
        @SerialName("season_type")
        val seasonType: Int = 0,
        @SerialName("stat")
        val stat: Stat = Stat()
    )

    @Serializable
    data class Section(
        @SerialName("attr")
        val attr: Int = 0,
        @SerialName("episode_id")
        val episodeId: Long = 0,
        @SerialName("episodes")
        val episodes: List<Episode> = listOf(),
        @SerialName("id")
        val id: Long = 0,
        @SerialName("title")
        val title: String = "",
        @SerialName("type")
        val type: Int = 0,
        @SerialName("type2")
        val type2: Int = 0
    )

    @Serializable
    data class Series(
        @SerialName("display_type")
        val displayType: Int = 0,
        @SerialName("series_id")
        val seriesId: Int = 0,
        @SerialName("series_title")
        val seriesTitle: String = ""
    )

    @Serializable
    data class UpInfo(
        @SerialName("avatar")
        val avatar: String = "",
        @SerialName("avatar_subscript_url")
        val avatarSubscriptUrl: String = "",
        @SerialName("follower")
        val follower: Int = 0,
        @SerialName("is_follow")
        val isFollow: Int = 0,
        @SerialName("mid")
        val mid: Long = 0,
        @SerialName("nickname_color")
        val nicknameColor: String = "",
        @SerialName("theme_type")
        val themeType: Int = 0,
        @SerialName("uname")
        val uname: String = "",
        @SerialName("verify_type")
        val verifyType: Int = 0,
        @SerialName("vip_status")
        val vipStatus: Int = 0,
        @SerialName("vip_type")
        val vipType: Int = 0
    ) {
        @Serializable
        data class Pendant(
            @SerialName("image")
            val image: String = "",
            @SerialName("name")
            val name: String = "",
            @SerialName("pid")
            val pid: Int = 0
        )
    }

    @Serializable
    data class UserStatus(
        @SerialName("area_limit")
        val areaLimit: Int = 0,
        @SerialName("ban_area_show")
        val banAreaShow: Int = 0,
        @SerialName("follow")
        val follow: Int = 0,
        @SerialName("follow_status")
        val followStatus: Int = 0,
        @SerialName("login")
        val login: Int = 0,
        @SerialName("pay")
        val pay: Int = 0,
        @SerialName("pay_pack_paid")
        val payPackPaid: Int = 0,
        @SerialName("sponsor")
        val sponsor: Int = 0
    )
}

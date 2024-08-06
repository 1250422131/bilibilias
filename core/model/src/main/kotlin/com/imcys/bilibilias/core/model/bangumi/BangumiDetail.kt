package com.imcys.bilibilias.core.model.bangumi

import com.imcys.bilibilias.core.model.user.Pendant
import com.imcys.bilibilias.core.model.video.Rights
import com.imcys.bilibilias.core.model.video.Stat
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class BangumiDetail(
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
    @SerialName("enable_vt")
    val enableVt: Boolean = false,
    @SerialName("episodes")
    val episodes: List<Episode> = listOf(),
    @SerialName("evaluate")
    val evaluate: String = "",
    @SerialName("freya")
    val freya: Freya = Freya(),
    @SerialName("hide_ep_vv_vt_dm")
    val hideEpVvVtDm: Int = 0,
    @SerialName("icon_font")
    val iconFont: IconFont = IconFont(),
    @SerialName("jp_title")
    val jpTitle: String = "",
    @SerialName("link")
    val link: String = "",
    @SerialName("media_id")
    val mediaId: Int = 0,
    @SerialName("mode")
    val mode: Int = 0,
    @SerialName("new_ep")
    val newEp: NewEp = NewEp(),
    @SerialName("payment")
    val payment: Payment = Payment(),
    @SerialName("play_strategy")
    val playStrategy: PlayStrategy = PlayStrategy(),
    @SerialName("positive")
    val positive: Positive = Positive(),
    @SerialName("publish")
    val publish: Publish = Publish(),
    @SerialName("rating")
    val rating: Rating = Rating(),
    @SerialName("record")
    val record: String = "",
    @SerialName("rights")
    val rights: Rights = Rights(),
    @SerialName("season_id")
    val seasonId: Int = 0,
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
    @SerialName("show")
    val show: Show = Show(),
    @SerialName("show_season_type")
    val showSeasonType: Int = 0,
    @SerialName("square_cover")
    val squareCover: String = "",
    @SerialName("staff")
    val staff: String = "",
    @SerialName("stat")
    val stat: Stat = Stat(),
    @SerialName("status")
    val status: Int = 0,
    @SerialName("styles")
    val styles: List<String> = listOf(),
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
    val userStatus: UserStatus = UserStatus(),
) {
    @Serializable
    data class Area(
        @SerialName("id")
        val id: Int = 0,
        @SerialName("name")
        val name: String = "",
    )

    @Serializable
    data class Freya(
        @SerialName("bubble_desc")
        val bubbleDesc: String = "",
        @SerialName("bubble_show_cnt")
        val bubbleShowCnt: Int = 0,
        @SerialName("icon_show")
        val iconShow: Int = 0,
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
        val vipPromotion: String = "",
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
            val forbidBb: Int = 0,
        )
    }

    @Serializable
    data class PlayStrategy(
        @SerialName("strategies")
        val strategies: List<String> = listOf(),
    )

    @Serializable
    data class Positive(
        @SerialName("id")
        val id: Int = 0,
        @SerialName("title")
        val title: String = "",
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
        val weekday: Int = 0,
    )

    @Serializable
    data class Rating(
        @SerialName("count")
        val count: Int = 0,
        @SerialName("score")
        val score: Double = 0.0,
    )

    @Serializable
    data class Season(
        @SerialName("badge")
        val badge: String = "",
        @SerialName("badge_type")
        val badgeType: Int = 0,
        @SerialName("cover")
        val cover: String = "",
        @SerialName("enable_vt")
        val enableVt: Boolean = false,
        @SerialName("horizontal_cover_1610")
        val horizontalCover1610: String = "",
        @SerialName("horizontal_cover_169")
        val horizontalCover169: String = "",
        @SerialName("icon_font")
        val iconFont: IconFont = IconFont(),
        @SerialName("media_id")
        val mediaId: Int = 0,
        @SerialName("new_ep")
        val newEp: NewEp = NewEp(),
        @SerialName("season_id")
        val seasonId: Long = 0,
        @SerialName("season_title")
        val seasonTitle: String = "",
        @SerialName("season_type")
        val seasonType: Int = 0,
        @SerialName("stat")
        val stat: Stat = Stat(),
    )

    @Serializable
    data class Section(
        @SerialName("attr")
        val attr: Int = 0,
        @SerialName("episode_id")
        val episodeId: Long = 0,
//        @SerialName("episode_ids")
//        val episodeIds: List<Any> = listOf(),
        @SerialName("episodes")
        val episodes: List<Episode> = listOf(),
        @SerialName("id")
        val id: Long = 0,
        @SerialName("title")
        val title: String = "",
        @SerialName("type")
        val type: Int = 0,
        @SerialName("type2")
        val type2: Int = 0,
    )

    @Serializable
    data class Series(
        @SerialName("display_type")
        val displayType: Int = 0,
        @SerialName("series_id")
        val seriesId: Int = 0,
        @SerialName("series_title")
        val seriesTitle: String = "",
    )

    @Serializable
    data class Show(
        @SerialName("wide_screen")
        val wideScreen: Int = 0,
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
        @SerialName("pendant")
        val pendant: Pendant = Pendant(),
        @SerialName("theme_type")
        val themeType: Int = 0,
        @SerialName("uname")
        val uname: String = "",
        @SerialName("verify_type")
        val verifyType: Int = 0,
        @SerialName("vip_status")
        val vipStatus: Int = 0,
        @SerialName("vip_type")
        val vipType: Int = 0,
    )

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
        val sponsor: Int = 0,
        @SerialName("vip_info")
        val vipInfo: VipInfo = VipInfo(),
    ) {
        @Serializable
        data class VipInfo(
            @SerialName("due_date")
            val dueDate: Long = 0,
            @SerialName("status")
            val status: Int = 0,
            @SerialName("type")
            val type: Int = 0,
        )
    }
}

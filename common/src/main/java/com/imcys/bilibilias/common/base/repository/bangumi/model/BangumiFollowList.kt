package com.imcys.bilibilias.common.base.repository.bangumi.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class BangumiFollowList(
    @SerialName("list")
    val list: List<Follow> = listOf(),
    @SerialName("pn")
    val pn: Int = 0,
    @SerialName("ps")
    val ps: Int = 0,
    @SerialName("total")
    val total: Int = 0
) {
    @Serializable
    data class Follow(
        @SerialName("areas")
        val areas: List<Area>? = listOf(),
        @SerialName("badge")
        val badge: String = "",
        @SerialName("badge_ep")
        val badgeEp: String = "",
        @SerialName("badge_info")
        val badgeInfo: BadgeInfo = BadgeInfo(),
        @SerialName("badge_infos")
        val badgeInfos: BadgeInfos? = BadgeInfos(),
        @SerialName("badge_type")
        val badgeType: Int = 0,
        @SerialName("both_follow")
        val bothFollow: Boolean = false,
        @SerialName("can_watch")
        val canWatch: Int = 0,
        @SerialName("cover")
        val cover: String = "",
        @SerialName("evaluate")
        val evaluate: String = "",
        @SerialName("first_ep")
        val firstEp: Int = 0,
        @SerialName("first_ep_info")
        val firstEpInfo: FirstEpInfo = FirstEpInfo(),
        @SerialName("follow_status")
        val followStatus: Int = 0,
        @SerialName("formal_ep_count")
        val formalEpCount: Int? = 0,
        @SerialName("horizontal_cover_16_10")
        val horizontalCover1610: String? = "",
        @SerialName("horizontal_cover_16_9")
        val horizontalCover169: String? = "",
        @SerialName("is_finish")
        val isFinish: Int = 0,
        @SerialName("is_new")
        val isNew: Int = 0,
        @SerialName("is_play")
        val isPlay: Int = 0,
        @SerialName("is_started")
        val isStarted: Int = 0,
        @SerialName("media_attr")
        val mediaAttr: Int = 0,
        @SerialName("media_id")
        val mediaId: Int = 0,
        @SerialName("mode")
        val mode: Int = 0,
        @SerialName("new_ep")
        val newEp: NewEp = NewEp(),
        @SerialName("producers")
        val producers: List<Producer>? = listOf(),
        @SerialName("progress")
        val progress: String = "",
        @SerialName("publish")
        val publish: Publish = Publish(),
        @SerialName("rating")
        val rating: Rating? = Rating(),
        @SerialName("renewal_time")
        val renewalTime: String? = "",
        @SerialName("rights")
        val rights: Rights = Rights(),
        @SerialName("season_attr")
        val seasonAttr: Int = 0,
        @SerialName("season_id")
        val seasonId: Int = 0,
        @SerialName("season_status")
        val seasonStatus: Int = 0,
        @SerialName("season_title")
        val seasonTitle: String = "",
        @SerialName("season_type")
        val seasonType: Int = 0,
        @SerialName("season_type_name")
        val seasonTypeName: String = "",
        @SerialName("season_version")
        val seasonVersion: String = "",
        @SerialName("section")
        val section: List<Section> = listOf(),
        @SerialName("series")
        val series: Series = Series(),
        @SerialName("short_url")
        val shortUrl: String = "",
        @SerialName("square_cover")
        val squareCover: String = "",
        @SerialName("stat")
        val stat: Stat = Stat(),
        @SerialName("styles")
        val styles: List<String> = listOf(),
        @SerialName("subtitle")
        val subtitle: String = "",
        @SerialName("subtitle_14")
        val subtitle14: String = "",
        @SerialName("subtitle_25")
        val subtitle25: String? = "",
        @SerialName("summary")
        val summary: String = "",
        @SerialName("title")
        val title: String = "",
        @SerialName("total_count")
        val totalCount: Int = 0,
        @SerialName("url")
        val url: String = "",
        @SerialName("viewable_crowd_type")
        val viewableCrowdType: Int = 0
    ) {
        @Serializable
        data class Area(
            @SerialName("id")
            val id: Int = 0,
            @SerialName("name")
            val name: String = ""
        )

        @Serializable
        data class BadgeInfo(
            @SerialName("bg_color")
            val bgColor: String = "",
            @SerialName("bg_color_night")
            val bgColorNight: String = "",
            @SerialName("img")
            val img: String? = "",
            @SerialName("text")
            val text: String? = ""
        )

        @Serializable
        data class BadgeInfos(
            @SerialName("content_attr")
            val contentAttr: ContentAttr? = ContentAttr(),
            @SerialName("vip_or_pay")
            val vipOrPay: VipOrPay = VipOrPay()
        ) {
            @Serializable
            data class ContentAttr(
                @SerialName("bg_color")
                val bgColor: String = "",
                @SerialName("bg_color_night")
                val bgColorNight: String = "",
                @SerialName("img")
                val img: String = "",
                @SerialName("text")
                val text: String = ""
            )

            @Serializable
            data class VipOrPay(
                @SerialName("bg_color")
                val bgColor: String = "",
                @SerialName("bg_color_night")
                val bgColorNight: String = "",
                @SerialName("img")
                val img: String = "",
                @SerialName("text")
                val text: String = ""
            )
        }

        @Serializable
        data class FirstEpInfo(
            @SerialName("cover")
            val cover: String = "",
            @SerialName("duration")
            val duration: Int = 0,
            @SerialName("id")
            val id: Int = 0,
            @SerialName("long_title")
            val longTitle: String? = "",
            @SerialName("pub_time")
            val pubTime: String = "",
            @SerialName("title")
            val title: String = ""
        )

        @Serializable
        data class NewEp(
            @SerialName("cover")
            val cover: String? = "",
            @SerialName("duration")
            val duration: Int? = 0,
            @SerialName("id")
            val id: Int? = 0,
            @SerialName("index_show")
            val indexShow: String = "",
            @SerialName("long_title")
            val longTitle: String? = "",
            @SerialName("pub_time")
            val pubTime: String? = "",
            @SerialName("title")
            val title: String? = ""
        )

        @Serializable
        data class Producer(
            @SerialName("is_contribute")
            val isContribute: Int? = 0,
            @SerialName("mid")
            val mid: Int = 0,
            @SerialName("type")
            val type: Int = 0
        )

        @Serializable
        data class Publish(
            @SerialName("pub_time")
            val pubTime: String = "",
            @SerialName("pub_time_show")
            val pubTimeShow: String = "",
            @SerialName("release_date")
            val releaseDate: String = "",
            @SerialName("release_date_show")
            val releaseDateShow: String = ""
        )

        @Serializable
        data class Rating(
            @SerialName("count")
            val count: Int = 0,
            @SerialName("score")
            val score: Double = 0.0
        )

        @Serializable
        data class Rights(
            @SerialName("allow_bp")
            val allowBp: Int? = 0,
            @SerialName("allow_preview")
            val allowPreview: Int? = 0,
            @SerialName("allow_review")
            val allowReview: Int? = 0,
            @SerialName("demand_end_time")
            val demandEndTime: DemandEndTime? = DemandEndTime(),
            @SerialName("is_rcmd")
            val isRcmd: Int = 0,
            @SerialName("is_selection")
            val isSelection: Int = 0,
            @SerialName("selection_style")
            val selectionStyle: Int = 0
        ) {
            @Serializable
            class DemandEndTime
        }

        @Serializable
        data class Section(
            @SerialName("attr")
            val attr: Int? = 0,
            @SerialName("ban_area_show")
            val banAreaShow: Int = 0,
            @SerialName("copyright")
            val copyright: String = "",
            @SerialName("episode_ids")
            val episodeIds: List<Int> = listOf(),
            @SerialName("limit_group")
            val limitGroup: Int = 0,
            @SerialName("season_id")
            val seasonId: Int = 0,
            @SerialName("section_id")
            val sectionId: Int = 0,
            @SerialName("title")
            val title: String? = "",
            @SerialName("type")
            val type: Int? = 0,
            @SerialName("watch_platform")
            val watchPlatform: Int = 0
        )

        @Serializable
        data class Series(
            @SerialName("new_season_id")
            val newSeasonId: Int = 0,
            @SerialName("season_count")
            val seasonCount: Int = 0,
            @SerialName("series_id")
            val seriesId: Int = 0,
            @SerialName("series_ord")
            val seriesOrd: Int = 0,
            @SerialName("title")
            val title: String = ""
        )

        @Serializable
        data class Stat(
            @SerialName("coin")
            val coin: Int = 0,
            @SerialName("danmaku")
            val danmaku: Int = 0,
            @SerialName("favorite")
            val favorite: Int = 0,
            @SerialName("follow")
            val follow: Int = 0,
            @SerialName("likes")
            val likes: Int = 0,
            @SerialName("reply")
            val reply: Int = 0,
            @SerialName("series_follow")
            val seriesFollow: Int = 0,
            @SerialName("series_view")
            val seriesView: Int = 0,
            @SerialName("view")
            val view: Int = 0
        )
    }
}

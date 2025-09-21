package com.imcys.bilibilias.network.model.user


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class BILIUserBangumiFollowInfo(
    @SerialName("list")
    val list: List<ItemData>,
    @SerialName("pn")
    val pn: Long,
    @SerialName("ps")
    val ps: Long,
    @SerialName("total")
    val total: Long
) {
    @Serializable
    data class ItemData(
        @SerialName("badge")
        val badge: String,
        @SerialName("badge_ep")
        val badgeEp: String,
        @SerialName("badge_info")
        val badgeInfo: BadgeInfo?,
        @SerialName("badge_infos")
        val badgeInfos: BadgeInfos?,
        @SerialName("badge_type")
        val badgeType: Long,
        @SerialName("both_follow")
        val bothFollow: Boolean,
        @SerialName("can_watch")
        val canWatch: Long,
        @SerialName("cover")
        val cover: String,
        @SerialName("evaluate")
        val evaluate: String,
        @SerialName("first_ep")
        val firstEp: Long,
        @SerialName("first_ep_info")
        val firstEpInfo: FirstEpInfo?,
        @SerialName("horizontal_cover_16_10")
        val horizontalCover1610: String?,
        @SerialName("horizontal_cover_16_9")
        val horizontalCover169: String?,
        @SerialName("is_finish")
        val isFinish: Long,
        @SerialName("is_new")
        val isNew: Long,
        @SerialName("is_play")
        val isPlay: Long,
        @SerialName("is_started")
        val isStarted: Long,
        @SerialName("media_attr")
        val mediaAttr: Long,
        @SerialName("media_id")
        val mediaId: Long,
        @SerialName("mode")
        val mode: Long,
        @SerialName("new_ep")
        val newEp: NewEp,
        @SerialName("progress")
        val progress: String,
        @SerialName("publish")
        val publish: Publish,
        @SerialName("rating")
        val rating: Rating?,
        @SerialName("renewal_time")
        val renewalTime: String?,
        @SerialName("rights")
        val rights: Rights?,
        @SerialName("season_attr")
        val seasonAttr: Long,
        @SerialName("season_id")
        val seasonId: Long,
        @SerialName("season_status")
        val seasonStatus: Long,
        @SerialName("season_title")
        val seasonTitle: String,
        @SerialName("season_type")
        val seasonType: Long,
        @SerialName("season_type_name")
        val seasonTypeName: String,
        @SerialName("season_version")
        val seasonVersion: String,
        @SerialName("series")
        val series: Series,
        @SerialName("short_url")
        val shortUrl: String,
        @SerialName("square_cover")
        val squareCover: String,
        @SerialName("stat")
        val stat: Stat,
        @SerialName("subtitle")
        val subtitle: String,
        @SerialName("subtitle_14")
        val subtitle14: String?,
        @SerialName("subtitle_25")
        val subtitle25: String?,
        @SerialName("summary")
        val summary: String,
        @SerialName("title")
        val title: String,
        @SerialName("total_count")
        val totalCount: Long,
        @SerialName("url")
        val url: String,
        @SerialName("viewable_crowd_type")
        val viewableCrowdType: Long
    ) {
        @Serializable
        data class Area(
            @SerialName("id")
            val id: Long,
            @SerialName("name")
            val name: String
        )

        @Serializable
        data class BadgeInfo(
            @SerialName("bg_color")
            val bgColor: String?,
            @SerialName("bg_color_night")
            val bgColorNight: String?,
            @SerialName("img")
            val img: String?,
            @SerialName("multi_img")
            val multiImg: MultiImg?,
            @SerialName("text")
            val text: String = ""
        )

        @Serializable
        data class BadgeInfos(
            @SerialName("content_attr")
            val contentAttr: ContentAttr?,
            @SerialName("vip_or_pay")
            val vipOrPay: VipOrPay?
        ) {
            @Serializable
            data class ContentAttr(
                @SerialName("bg_color")
                val bgColor: String,
                @SerialName("bg_color_night")
                val bgColorNight: String,
                @SerialName("img")
                val img: String,
                @SerialName("multi_img")
                val multiImg: MultiImg?,
                @SerialName("text")
                val text: String
            )

            @Serializable
            data class VipOrPay(
                @SerialName("bg_color")
                val bgColor: String,
                @SerialName("bg_color_night")
                val bgColorNight: String,
                @SerialName("img")
                val img: String,
                @SerialName("multi_img")
                val multiImg: MultiImg?,
                @SerialName("text")
                val text: String
            )
        }

        @Serializable
        data class ConfigAttrs(
            @SerialName("cc_on_lock")
            val ccOnLock: CcOnLock?,
            @SerialName("highlight_ineffective_hd")
            val highlightIneffectiveHd: HighlightIneffectiveHd?,
            @SerialName("highlight_ineffective_ott")
            val highlightIneffectiveOtt: HighlightIneffectiveOtt?,
            @SerialName("highlight_ineffective_pink")
            val highlightIneffectivePink: HighlightIneffectivePink?,
            @SerialName("max_resolution_without_drm")
            val maxResolutionWithoutDrm: MaxResolutionWithoutDrm?
        ) {
            @Serializable
            data class CcOnLock(
                @SerialName("type_url")
                val typeUrl: String
            )

            @Serializable
            data class DrmSupported(
                @SerialName("type_url")
                val typeUrl: String,
                @SerialName("value")
                val value: String?
            )

            @Serializable
            data class HighlightIneffectiveHd(
                @SerialName("type_url")
                val typeUrl: String
            )

            @Serializable
            data class HighlightIneffectiveOtt(
                @SerialName("type_url")
                val typeUrl: String
            )

            @Serializable
            data class HighlightIneffectivePink(
                @SerialName("type_url")
                val typeUrl: String
            )

            @Serializable
            data class MaxResolutionWithoutDrm(
                @SerialName("type_url")
                val typeUrl: String,
                @SerialName("value")
                val value: String
            )
        }

        @Serializable
        data class FirstEpInfo(
            @SerialName("cover")
            val cover: String,
            @SerialName("duration")
            val duration: Long,
            @SerialName("id")
            val id: Long,
            @SerialName("long_title")
            val longTitle: String = "",
            @SerialName("pub_time")
            val pubTime: String,
            @SerialName("title")
            val title: String
        )

        @Serializable
        data class NewEp(
            @SerialName("cover")
            val cover: String?,
            @SerialName("duration")
            val duration: Long?,
            @SerialName("id")
            val id: Long?,
            @SerialName("index_show")
            val indexShow: String?,
            @SerialName("long_title")
            val longTitle: String = "",
            @SerialName("pub_time")
            val pubTime: String?,
            @SerialName("title")
            val title: String = ""
        )

        @Serializable
        data class Producer(
            @SerialName("is_contribute")
            val isContribute: Long?,
            @SerialName("mid")
            val mid: Long?,
            @SerialName("title")
            val title: String?,
            @SerialName("type")
            val type: Long?
        )

        @Serializable
        data class Publish(
            @SerialName("pub_time")
            val pubTime: String,
            @SerialName("pub_time_show")
            val pubTimeShow: String,
            @SerialName("release_date")
            val releaseDate: String,
            @SerialName("release_date_show")
            val releaseDateShow: String
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
            @SerialName("allow_preview")
            val allowPreview: Long?,
            @SerialName("allow_review")
            val allowReview: Long?,
            @SerialName("is_rcmd")
            val isRcmd: Long?,
            @SerialName("is_selection")
            val isSelection: Long?,
            @SerialName("selection_style")
            val selectionStyle: Long?
        )

        @Serializable
        data class Section(
            @SerialName("attr")
            val attr: Long?,
            @SerialName("ban_area_show")
            val banAreaShow: Long,
            @SerialName("copyright")
            val copyright: String,
            @SerialName("episode_ids")
            val episodeIds: List<Long> = emptyList(),
            @SerialName("limit_group")
            val limitGroup: Long,
            @SerialName("season_id")
            val seasonId: Long,
            @SerialName("section_id")
            val sectionId: Long,
            @SerialName("title")
            val title: String?,
            @SerialName("type")
            val type: Long?,
            @SerialName("watch_platform")
            val watchPlatform: Long
        )

        @Serializable
        data class Series(
            @SerialName("new_season_id")
            val newSeasonId: Long,
            @SerialName("season_count")
            val seasonCount: Long,
            @SerialName("series_id")
            val seriesId: Long,
            @SerialName("series_ord")
            val seriesOrd: Long?,
            @SerialName("title")
            val title: String
        )

        @Serializable
        data class Stat(
            @SerialName("coin")
            val coin: Long,
            @SerialName("danmaku")
            val danmaku: Long,
            @SerialName("favorite")
            val favorite: Long,
            @SerialName("follow")
            val follow: Long,
            @SerialName("likes")
            val likes: Long,
            @SerialName("reply")
            val reply: Long,
            @SerialName("series_follow")
            val seriesFollow: Long,
            @SerialName("series_view")
            val seriesView: Long,
            @SerialName("view")
            val view: Long
        )
    }
    
}

@Serializable
data class MultiImg(
    @SerialName("color")
    val color: String?,
    @SerialName("medium_remind")
    val mediumRemind: String?
)
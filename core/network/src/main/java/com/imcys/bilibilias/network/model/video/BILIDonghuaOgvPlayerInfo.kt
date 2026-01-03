package com.imcys.bilibilias.network.model.video
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName


@Serializable
data class BILIDonghuaOgvPlayerInfo(
    @SerialName("arc")
    val arc: Arc,
    @SerialName("play_video_type")
    val playVideoType: String,
    @SerialName("plugins")
    val plugins: List<Plugin>,
    @SerialName("supplement")
    val supplement: Supplement,
    @SerialName("user_status")
    val userStatus: UserStatus,
    @SerialName("video_info")
    val videoInfo: BILIDonghuaPlayerInfo,
    @SerialName("watch_progress")
    val watchProgress: WatchProgress
) {
    @Serializable
    data class Arc(
        @SerialName("aid")
        val aid: Long,
        @SerialName("biz_type")
        val bizType: Int,
        @SerialName("bvid")
        val bvid: String,
        @SerialName("cid")
        val cid: Long
    )

    @Serializable
    data class Plugin(
        @SerialName("config")
        val config: Config,
        @SerialName("name")
        val name: String
    ) {
        @Serializable
        data class Config(
            @SerialName("data")
            val `data`: String?,
            @SerialName("hide")
            val hide: Hide?,
            @SerialName("show")
            val show: Show
        ) {
            @Serializable
            data class Hide(
                @SerialName("type")
                val type: String,
                @SerialName("value")
                val value: String
            )

            @Serializable
            data class Show(
                @SerialName("count")
                val count: Int?,
                @SerialName("type")
                val type: String,
                @SerialName("value")
                val value: String
            )
        }
    }

    @Serializable
    data class Supplement(
        @SerialName("ogv_episode_info")
        val ogvEpisodeInfo: OgvEpisodeInfo,
        @SerialName("ogv_pay_tip")
        val ogvPayTip: OgvPayTip,
        @SerialName("ogv_season_info")
        val ogvSeasonInfo: OgvSeasonInfo,
        @SerialName("ogv_season_watch_progress")
        val ogvSeasonWatchProgress: OgvSeasonWatchProgress?,
        @SerialName("record_number")
        val recordNumber: RecordNumber
    ) {
        @Serializable
        data class OgvEpisodeInfo(
            @SerialName("episode_id")
            val episodeId: Int,
            @SerialName("episode_status")
            val episodeStatus: Int,
            @SerialName("index_title")
            val indexTitle: String?,
            @SerialName("long_title")
            val longTitle: String?
        )

        @Serializable
        data class OgvPayTip(
            @SerialName("angle_style")
            val angleStyle: Int,
            @SerialName("bg_day_color")
            val bgDayColor: String,
            @SerialName("bg_line_color")
            val bgLineColor: String,
            @SerialName("bg_night_color")
            val bgNightColor: String,
            @SerialName("bg_night_line_color")
            val bgNightLineColor: String,
            @SerialName("giant_screen_img")
            val giantScreenImg: String,
            @SerialName("icon")
            val icon: String,
            @SerialName("img")
            val img: String,
            @SerialName("jump_type")
            val jumpType: String,
            @SerialName("link")
            val link: String,
            @SerialName("link_open_type")
            val linkOpenType: Int,
            @SerialName("order_report_params")
            val orderReportParams: OrderReportParams,
            @SerialName("pc_link")
            val pcLink: String,
            @SerialName("report")
            val report: Report,
            @SerialName("report_params")
            val reportParams: ReportParams,
            @SerialName("report_type")
            val reportType: Int,
            @SerialName("show_type")
            val showType: Int,
            @SerialName("text_color")
            val textColor: String,
            @SerialName("text_night_color")
            val textNightColor: String,
            @SerialName("title")
            val title: String,
            @SerialName("type")
            val type: Int,
            @SerialName("view_start_time")
            val viewStartTime: Int
        ) {
            @Serializable
            data class OrderReportParams(
                @SerialName("ep_status")
                val epStatus: String,
                @SerialName("epid")
                val epid: String,
                @SerialName("exp_group_tag")
                val expGroupTag: String,
                @SerialName("exp_tag")
                val expTag: String,
                @SerialName("material_type")
                val materialType: String,
                @SerialName("position_id")
                val positionId: String,
                @SerialName("request_id")
                val requestId: String,
                @SerialName("season_id")
                val seasonId: String,
                @SerialName("season_status")
                val seasonStatus: String,
                @SerialName("season_type")
                val seasonType: String,
                @SerialName("tips_id")
                val tipsId: String,
                @SerialName("tips_repeat_key")
                val tipsRepeatKey: String,
                @SerialName("unit_id")
                val unitId: String,
                @SerialName("vip_status")
                val vipStatus: String,
                @SerialName("vip_type")
                val vipType: String
            )

            @Serializable
            data class Report(
                @SerialName("click_event_id")
                val clickEventId: String,
                @SerialName("extend")
                val extend: String,
                @SerialName("show_event_id")
                val showEventId: String
            )

            @Serializable
            data class ReportParams(
                @SerialName("banner_type")
                val bannerType: String,
                @SerialName("ep_status")
                val epStatus: String,
                @SerialName("epid")
                val epid: String,
                @SerialName("exp_group_tag")
                val expGroupTag: String,
                @SerialName("exp_tag")
                val expTag: String,
                @SerialName("material_type")
                val materialType: String,
                @SerialName("position_id")
                val positionId: String,
                @SerialName("request_id")
                val requestId: String,
                @SerialName("season_id")
                val seasonId: String,
                @SerialName("season_status")
                val seasonStatus: String,
                @SerialName("season_type")
                val seasonType: String,
                @SerialName("tips_id")
                val tipsId: String,
                @SerialName("tips_repeat_key")
                val tipsRepeatKey: String,
                @SerialName("unit_id")
                val unitId: String,
                @SerialName("vip_status")
                val vipStatus: String,
                @SerialName("vip_type")
                val vipType: String
            )
        }

        @Serializable
        data class OgvSeasonInfo(
            @SerialName("season_id")
            val seasonId: Int,
            @SerialName("season_type")
            val seasonType: Int
        )

        @Serializable
        data class OgvSeasonWatchProgress(
            @SerialName("last_ep_id")
            val lastEpId: Int,
            @SerialName("last_ep_index_title")
            val lastEpIndexTitle: String,
            @SerialName("last_ep_progress")
            val lastEpProgress: Int
        )

        @Serializable
        class RecordNumber
    }

    @Serializable
    data class UserStatus(
        @SerialName("follow_info")
        val followInfo: FollowInfo?,
        @SerialName("is_login")
        val isLogin: Boolean,
        @SerialName("vip_info")
        val vipInfo: VipInfo
    ) {
        @Serializable
        data class FollowInfo(
            @SerialName("follow")
            val follow: Boolean,
            @SerialName("follow_status")
            val followStatus: Int
        )

        @Serializable
        data class VipInfo(
            @SerialName("due_date")
            val dueDate: Long?,
            @SerialName("status")
            val status: Int?,
            @SerialName("type")
            val type: Int?
        )
    }

    @Serializable
    data class WatchProgress(
        @SerialName("current_progress")
        val currentProgress: Int?
    )
}

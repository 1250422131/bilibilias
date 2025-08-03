package com.imcys.bilibilias.network.model.user


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable



@Serializable
data class BILISpaceArchiveInfo(
    @SerialName("episodic_button")
    val episodicButton: EpisodicButton?,
    @SerialName("list")
    val list: MList,
    @SerialName("page")
    val page: Page
) {
    @Serializable
    data class EpisodicButton(
        @SerialName("text")
        val text: String,
        @SerialName("uri")
        val uri: String
    )

    @Serializable
    data class MList(
        @SerialName("vlist")
        val vlist: List<Vlist>
    ) {
        @Serializable
        data class Vlist(
            @SerialName("aid")
            val aid: Long,
            @SerialName("attribute")
            val attribute: Long,
            @SerialName("author")
            val author: String,
            @SerialName("bvid")
            val bvid: String,
            @SerialName("comment")
            val comment: Long,
            @SerialName("copyright")
            val copyright: String,
            @SerialName("created")
            val created: Long,
            @SerialName("description")
            val description: String,
            @SerialName("elec_arc_badge")
            val elecArcBadge: String,
            @SerialName("elec_arc_type")
            val elecArcType: Long,
            @SerialName("enable_vt")
            val enableVt: Long,
            @SerialName("hide_click")
            val hideClick: Boolean,
            @SerialName("is_avoided")
            val isAvoided: Long,
            @SerialName("is_charging_arc")
            val isChargingArc: Boolean,
            @SerialName("is_lesson_finished")
            val isLessonFinished: Long,
            @SerialName("is_lesson_video")
            val isLessonVideo: Long,
            @SerialName("is_live_playback")
            val isLivePlayback: Long,
            @SerialName("is_pay")
            val isPay: Long,
            @SerialName("is_self_view")
            val isSelfView: Boolean,
            @SerialName("is_steins_gate")
            val isSteinsGate: Long,
            @SerialName("is_union_video")
            val isUnionVideo: Long,
            @SerialName("jump_url")
            val jumpUrl: String,
            @SerialName("length")
            val length: String,
            @SerialName("lesson_update_info")
            val lessonUpdateInfo: String,
            @SerialName("meta")
            val meta: Meta?,
            @SerialName("mid")
            val mid: Long,
            @SerialName("pic")
            val pic: String,
            @SerialName("play")
            val play: Long,
            @SerialName("playback_position")
            val playbackPosition: Long,
            @SerialName("review")
            val review: Long,
            @SerialName("season_id")
            val seasonId: Long,
            @SerialName("subtitle")
            val subtitle: String,
            @SerialName("title")
            val title: String,
            @SerialName("typeid")
            val typeid: Long,
            @SerialName("video_review")
            val videoReview: Long,
            @SerialName("vt")
            val vt: Long,
            @SerialName("vt_display")
            val vtDisplay: String
        ) {
            @Serializable
            data class Meta(
                @SerialName("attribute")
                val attribute: Long,
                @SerialName("cover")
                val cover: String,
                @SerialName("ep_count")
                val epCount: Long,
                @SerialName("ep_num")
                val epNum: Long,
                @SerialName("first_aid")
                val firstAid: Long,
                @SerialName("id")
                val id: Long,
                @SerialName("Longro")
                val Longro: String = "",
                @SerialName("mid")
                val mid: Long,
                @SerialName("ptime")
                val ptime: Long,
                @SerialName("sign_state")
                val signState: Long,
                @SerialName("stat")
                val stat: Stat,
                @SerialName("title")
                val title: String
            ) {
                @Serializable
                data class Stat(
                    @SerialName("coin")
                    val coin: Long,
                    @SerialName("danmaku")
                    val danmaku: Long,
                    @SerialName("favorite")
                    val favorite: Long,
                    @SerialName("like")
                    val like: Long,
                    @SerialName("mtime")
                    val mtime: Long,
                    @SerialName("reply")
                    val reply: Long,
                    @SerialName("season_id")
                    val seasonId: Long,
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
        }
    }

    @Serializable
    data class Page(
        @SerialName("count")
        val count: Long,
        @SerialName("pn")
        val pn: Long,
        @SerialName("ps")
        val ps: Long
    )
}


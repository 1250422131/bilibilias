package com.imcys.bilibilias.core.model.space

import com.imcys.bilibilias.core.model.Page
import com.imcys.bilibilias.core.model.video.Aid
import com.imcys.bilibilias.core.model.video.Bvid
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SpaceArcSearch(
    @SerialName("list")
    val list: List = List(),
    @SerialName("page")
    val page: Page = Page()
) {

    @Serializable
    data class List(
        @SerialName("vlist")
        val vlist: kotlin.collections.List<Vlist> = kotlin.collections.listOf()
    ) {
        @Serializable
        data class Vlist(
            @SerialName("aid")
            val aid: Aid = 0,
            @SerialName("attribute")
            val attribute: Int = 0,
            @SerialName("author")
            val author: String = "",
            @SerialName("bvid")
            val bvid: Bvid = "",
            @SerialName("comment")
            val comment: Int = 0,
            @SerialName("copyright")
            val copyright: String = "",
            @SerialName("created")
            val created: Long = 0,
            @SerialName("description")
            val description: String = "",
            @SerialName("enable_vt")
            val enableVt: Int = 0,
            @SerialName("hide_click")
            val hideClick: Boolean = false,
            @SerialName("is_avoided")
            val isAvoided: Int = 0,
            @SerialName("is_charging_arc")
            val isChargingArc: Boolean = false,
            @SerialName("is_lesson_finished")
            val isLessonFinished: Int = 0,
            @SerialName("is_lesson_video")
            val isLessonVideo: Int = 0,
            @SerialName("is_live_playback")
            val isLivePlayback: Int = 0,
            @SerialName("is_pay")
            val isPay: Int = 0,
            @SerialName("is_steins_gate")
            val isSteinsGate: Int = 0,
            @SerialName("is_union_video")
            val isUnionVideo: Int = 0,
            @SerialName("jump_url")
            val jumpUrl: String = "",
            @SerialName("length")
            val length: String = "",
            @SerialName("lesson_update_info")
            val lessonUpdateInfo: String = "",
            @SerialName("mid")
            val mid: Long = 0,
            @SerialName("pic")
            val pic: String = "",
            @SerialName("play")
            val play: Int = 0,
            @SerialName("playback_position")
            val playbackPosition: Int = 0,
            @SerialName("review")
            val review: Int = 0,
            @SerialName("season_id")
            val seasonId: Long = 0,
            @SerialName("subtitle")
            val subtitle: String = "",
            @SerialName("title")
            val title: String = "",
            @SerialName("typeid")
            val typeid: Int = 0,
            @SerialName("video_review")
            val videoReview: Int = 0,
            @SerialName("vt")
            val vt: Int = 0,
            @SerialName("vt_display")
            val vtDisplay: String = ""
        )
    }
}

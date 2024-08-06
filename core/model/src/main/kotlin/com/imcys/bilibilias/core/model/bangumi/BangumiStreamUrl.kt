package com.imcys.bilibilias.core.model.bangumi

import com.imcys.bilibilias.core.model.video.Audio
import com.imcys.bilibilias.core.model.video.SupportFormat
import com.imcys.bilibilias.core.model.video.Video
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class BangumiStreamUrl(
    @SerialName("video_info")
    val videoInfo: VideoInfo = VideoInfo(),
) {
    @Serializable
    data class VideoInfo(
        @SerialName("accept_description")
        val acceptDescription: List<String> = listOf(),
        @SerialName("accept_format")
        val acceptFormat: String = "",
        @SerialName("accept_quality")
        val acceptQuality: List<Int> = listOf(),
        @SerialName("bp")
        val bp: Int = 0,
        @SerialName("clip_info_list")
        val clipInfoList: List<ClipInfo> = listOf(),
        @SerialName("code")
        val code: Int = 0,
        @SerialName("dash")
        val dash: Dash = Dash(),
        @SerialName("fnval")
        val fnval: Int = 0,
        @SerialName("fnver")
        val fnver: Int = 0,
        @SerialName("format")
        val format: String = "",
        @SerialName("from")
        val from: String = "",
        @SerialName("has_paid")
        val hasPaid: Boolean = false,
        @SerialName("is_drm")
        val isDrm: Boolean = false,
        @SerialName("is_preview")
        val isPreview: Int = 0,
        @SerialName("message")
        val message: String = "",
        @SerialName("no_rexcode")
        val noRexcode: Int = 0,
        @SerialName("quality")
        val quality: Int = 0,
        @SerialName("record_info")
        val recordInfo: Dash.RecordInfo = Dash.RecordInfo(),
        @SerialName("result")
        val result: String = "",
        @SerialName("seek_param")
        val seekParam: String = "",
        @SerialName("seek_type")
        val seekType: String = "",
        @SerialName("status")
        val status: Int = 0,
        @SerialName("support_formats")
        val supportFormats: List<SupportFormat> = listOf(),
        @SerialName("timelength")
        val timelength: Int = 0,
        @SerialName("type")
        val type: String = "",
        @SerialName("video_codecid")
        val videoCodecid: Int = 0,
        @SerialName("video_project")
        val videoProject: Boolean = false
    ) {
        @Serializable
        data class ClipInfo(
            @SerialName("clipType")
            val clipType: String = "",
            @SerialName("end")
            val end: Int = 0,
            @SerialName("materialNo")
            val materialNo: Int = 0,
            @SerialName("start")
            val start: Int = 0,
            @SerialName("toastText")
            val toastText: String = ""
        )

        @Serializable
        data class Dash(
            @SerialName("audio")
            val audio: List<Audio> = listOf(),
            @SerialName("duration")
            val duration: Int = 0,
            @SerialName("min_buffer_time")
            val minBufferTime: Double = 0.0,
            @SerialName("video")
            val video: List<Video> = listOf()
        ) {
            @Serializable
            data class RecordInfo(
                @SerialName("record")
                val record: String = "",
                @SerialName("record_icon")
                val recordIcon: String = ""
            )
        }
    }
}

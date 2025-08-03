package com.imcys.bilibilias.core.datasource.model


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class VideoPlaybackInfo(
    @SerialName("accept_description")
    val acceptDescription: List<String> = listOf(),
    @SerialName("accept_format")
    val acceptFormat: String = "",
    @SerialName("accept_quality")
    val acceptQuality: List<Int> = listOf(),
    @SerialName("dash")
    val dash: Dash = Dash(),
    @SerialName("format")
    val format: String = "",
    @SerialName("from")
    val from: String = "",
    @SerialName("last_play_cid")
    val lastPlayCid: Long = 0,
    @SerialName("last_play_time")
    val lastPlayTime: Int = 0,
    @SerialName("message")
    val message: String = "",
    @SerialName("quality")
    val quality: Int = 0,
    @SerialName("result")
    val result: String = "",
    @SerialName("seek_param")
    val seekParam: String = "",
    @SerialName("seek_type")
    val seekType: String = "",
    @SerialName("support_formats")
    val supportFormats: List<Dash.SupportFormat> = listOf(),
    @SerialName("timelength")
    val timeLength: Int = 0,
    @SerialName("video_codecid")
    val videoCodecId: Int = 0,
) {
    @Serializable
    data class Dash(
        @SerialName("audio")
        val audio: List<AudioOrVideo> = listOf(),
        @SerialName("duration")
        val duration: Int = 0,
        @SerialName("min_buffer_time")
        val minBufferTime: Double = 0.0,
        @SerialName("video")
        val video: List<AudioOrVideo> = listOf()
    ) {
        @Serializable
        data class SupportFormat(
            @SerialName("codecs")
            val codecs: List<String> = listOf(),
            @SerialName("display_desc")
            val displayDesc: String = "",
            @SerialName("format")
            val format: String = "",
            @SerialName("new_description")
            val newDescription: String = "",
            @SerialName("quality")
            val quality: Int = 0,
            @SerialName("superscript")
            val superscript: String = ""
        )
    }

    @Serializable
    data class AudioOrVideo(
        @SerialName("base_url")
        val baseUrl: String = "",
        @SerialName("backup_url")
        val backupUrl1: List<String> = listOf(),
        @SerialName("backupUrl")
        val backupUrl2: List<String> = listOf(),
        @SerialName("bandwidth")
        val bandwidth: Int = 0,
        @SerialName("codecid")
        val codecid: Int = 0,
        @SerialName("codecs")
        val codecs: String = "",
        @SerialName("frame_rate")
        val frameRate: String = "",
        @SerialName("height")
        val height: Int = 0,
        @SerialName("id")
        val id: Int = 0,
        @SerialName("mime_type")
        val mimeType: String = "",
        @SerialName("sar")
        val sar: String = "",
        @SerialName("segment_base")
        val segmentBase: SegmentBase = SegmentBase(),
        @SerialName("start_with_sap")
        val startWithSap: Int = 0,
        @SerialName("width")
        val width: Int = 0
    ) {
        @Serializable
        data class SegmentBase(
            @SerialName("index_range")
            val indexRange: String = "",
            @SerialName("initialization")
            val initialization: String = ""
        )
    }
}
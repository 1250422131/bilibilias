package com.imcys.bilibilias.core.datasource.model


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

@Serializable
data class VideoPlaybackInfo(
    @SerialName("accept_description")
    val acceptDescription: List<String>,
    @SerialName("accept_format")
    val acceptFormat: String,
    @SerialName("accept_quality")
    val acceptQuality: List<Int>,
    @SerialName("dash")
    val dash: Dash,
    @SerialName("format")
    val format: String,
    @SerialName("from")
    val from: String,
    @SerialName("last_play_cid")
    val lastPlayCid: Long,
    @SerialName("last_play_time")
    val lastPlayTime: Int,
    @SerialName("message")
    val message: String,
    @SerialName("quality")
    val quality: Int,
    @SerialName("result")
    val result: String,
    @SerialName("seek_param")
    val seekParam: String,
    @SerialName("seek_type")
    val seekType: String,
    @SerialName("support_formats")
    val supportFormats: List<Dash.SupportFormat>,
    @SerialName("timelength")
    val timeLength: Int,
    @SerialName("video_codecid")
    val videoCodecId: Int,
) {
    @Serializable
    data class Dash(
        @SerialName("audio")
        val audio: List<AudioOrVideo>,
        @SerialName("dolby")
        val dolby: Dolby,
        @SerialName("duration")
        val duration: Int,
        @SerialName("flac")
        val flac: Flac? = null,
        @SerialName("video")
        val video: List<AudioOrVideo>,
    ) {
        @Transient
        val combinedAudioSources = buildList {
            addAll(audio)
            flac?.audio?.let { add(it) }
            dolby.audio?.let { addAll(it) }
        }
        @Serializable
        data class Dolby(
            @SerialName("audio")
            val audio: List<AudioOrVideo>? = null,
        )

        @Serializable
        data class Flac(
            @SerialName("audio")
            val audio: AudioOrVideo,
        )

        @Serializable
        data class SupportFormat(
            @SerialName("codecs")
            val codecs: List<String>,
            @SerialName("display_desc")
            val displayDesc: String,
            @SerialName("format")
            val format: String,
            @SerialName("new_description")
            val newDescription: String,
            @SerialName("quality")
            val quality: Int,
            @SerialName("superscript")
            val superscript: String,
        )
    }

    @Serializable
    data class AudioOrVideo(
        @SerialName("base_url")
        val baseUrl: String,
        @SerialName("backup_url")
        val primaryBackupUrls: List<String>,
        @SerialName("backupUrl")
        val secondaryBackupUrls: List<String>,
        @SerialName("bandwidth")
        val bandwidth: Int,
        @SerialName("codecid")
        val codecid: Int,
        @SerialName("codecs")
        val codecs: String,
        @SerialName("frame_rate")
        val frameRate: String,
        @SerialName("height")
        val height: Int,
        @SerialName("id")
        val id: Int,
        @SerialName("mime_type")
        val mimeType: String,
        @SerialName("sar")
        val sar: String,
        @SerialName("segment_base")
        val segmentBase: SegmentBase,
        @SerialName("start_with_sap")
        val startWithSap: Int,
        @SerialName("width")
        val width: Int,
    ) {
        @Serializable
        data class SegmentBase(
            @SerialName("index_range")
            val indexRange: String,
            @SerialName("initialization")
            val initialization: String,
        )
    }
}
package com.imcys.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * dash类型视频返回数据
 *
 * ![DASH格式](https://github.com/SocialSisterYi/bilibili-API-collect/blob/master/docs/video/videostream_url.md#dash%E6%A0%BC%E5%BC%8F)
 * todo dash部分类的字段或许是缺失的
 */
@Serializable
data class PlayerInfo(
    @SerialName("accept_description")
    val acceptDescription: List<String> = listOf(),
    @SerialName("accept_quality")
    val acceptQuality: List<Int> = listOf(),
    @SerialName("dash")
    val dash: Dash = Dash(),
    @SerialName("format")
    val format: String = "", // flv480
    @SerialName("message")
    val message: String = "",
    @SerialName("quality")
    val quality: Int = 0, // 32
    @SerialName("support_formats")
    val supportFormats: List<SupportFormat> = listOf(),
    @SerialName("timelength")
    val timelength: Int = 0, // 53962
    @SerialName("video_codecid")
    val videoCodecid: Int = 0
)

@Serializable
data class Dash(
    @SerialName("audio")
    val audio: List<Audio> = listOf(),
    @SerialName("dolby")
    val dolby: Dolby = Dolby(),
    @SerialName("duration")
    val duration: Int = 0, // 54
    @SerialName("min_buffer_time")
    val minBufferTime: Double = 0.0, // 1.5
    @SerialName("video")
    val video: List<Video> = listOf()
) {
    @Serializable
    data class Audio(
        @SerialName("backup_url")
        val backupUrl: List<String> = listOf(),
        @SerialName("bandwidth")
        val bandwidth: Int = 0, // 40882
        @SerialName("base_url")
        val baseUrl: String = "",
        @SerialName("codecid")
        val codecid: Int = 0, // 0
        @SerialName("codecs")
        val codecs: String = "", // mp4a.40.5
        @SerialName("frame_rate")
        val frameRate: String = "",
        @SerialName("height")
        val height: Int = 0, // 0
        @SerialName("id")
        val id: Int = 0, // 30216
        @SerialName("mime_type")
        val mimeType: String = "", // audio/mp4
        @SerialName("sar")
        val sar: String = "",
        @SerialName("segment_base")
        val segmentBase: SegmentBase = SegmentBase(),
        @SerialName("start_with_sap")
        val startWithSap: Int = 0, // 0
        @SerialName("width")
        val width: Int = 0 // 0
    )

    @Serializable
    data class Dolby(
        @SerialName("type")
        val type: Int = 0, // 0
        val audio: List<Audio>? = null
    )

    @Serializable
    data class Video(
        @SerialName("backup_url")
        val backupUrl: List<String> = listOf(),
        @SerialName("bandwidth")
        val bandwidth: Int = 0, // 126915
        @SerialName("base_url")
        val baseUrl: String = "",
        @SerialName("codecid")
        val codecid: Int = 0, // 7
        @SerialName("codecs")
        val codecs: String = "", // avc1.64001F
        @SerialName("frame_rate")
        val frameRate: Float = 0F, // 23.810
        @SerialName("height")
        val height: Int = 0, // 480
        @SerialName("id")
        val id: Int = 0, // 32
        @SerialName("mime_type")
        val mimeType: String = "", // video/mp4
        @SerialName("sar")
        val sar: String = "", // 1:1
        @SerialName("segment_base")
        val segmentBase: SegmentBase = SegmentBase(),
        @SerialName("start_with_sap")
        val startWithSap: Int = 0, // 1
        @SerialName("width")
        val width: Int = 0 // 688
    )
}
@Serializable
data class SegmentBase(
    @SerialName("index_range")
    val indexRange: String = "", // 1007-1170
    @SerialName("initialization")
    val initialization: String = "" // 0-1006
)
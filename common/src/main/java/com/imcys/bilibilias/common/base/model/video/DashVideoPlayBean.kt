package com.imcys.bilibilias.common.base.model.video

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import timber.log.Timber

/**
 * dash类型视频返回数据
 *
 * ![DASH格式](https://github.com/SocialSisterYi/bilibili-API-collect/blob/master/docs/video/videostream_url.md#dash%E6%A0%BC%E5%BC%8F)
 * todo dash部分类的字段或许是缺失的
 */
@Serializable
data class DashVideoPlayBean(
    @SerialName("accept_description")
    val acceptDescription: List<String> = listOf(),
    @SerialName("accept_format")
    val acceptFormat: String = "", // flv480,mp4
    @SerialName("accept_quality")
    val acceptQuality: List<Int> = listOf(),
    @SerialName("dash")
    val dash: Dash = Dash(),
    @SerialName("format")
    val format: String = "", // flv480
    @SerialName("from")
    val from: String = "", // local
    @SerialName("last_play_cid")
    val lastPlayCid: Int = 0, // 0
    @SerialName("last_play_time")
    val lastPlayTime: Int = 0, // 0
    @SerialName("message")
    val message: String = "",
    @SerialName("quality")
    val quality: Int = 0, // 32
    @SerialName("result")
    val result: String = "", // suee
    @SerialName("seek_param")
    val seekParam: String = "", // start
    @SerialName("seek_type")
    val seekType: String = "", // offset
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
        val baseUrl: String = "", // https://xy182x125x174x46xy.mcdn.bilivideo.cn:8082/v1/resource/1260927673-1-30216.m4s?agrr=0&build=0&buvid=&bvc=vod&bw=5203&cdnid=11153&deadline=1695274017&e=ig8euxZM2rNcNbdlhoNvNC8BqJIzNbfqXBvEqxTEto8BTrNvN0GvT90W5JZMkX_YN0MvXg8gNEV4NC8xNEV4N03eN0B5tZlqNxTEto8BTrNvNeZVuJ10Kj_g2UB02J0mN0B5tZlqNCNEto8BTrNvNC7MTX502C8f2jmMQJ6mqF2fka1mqx6gqj0eN0B599M%3D&f=u_0_0&gen=playurlv2&logo=80000000&mid=10993030&nbs=1&nettype=0&oi=1974978899&orderid=0%2C3&os=bcache&platform=pc&sign=bc00e9&traceid=trBGlREsNdjFPi_0_e_N&uipk=5&uparams=e%2Cuipk%2Cnbs%2Cdeadline%2Cgen%2Cos%2Coi%2Ctrid%2Cmid%2Cplatform&upsig=21e011b0b7becf4d99c918da87af07da
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
    ) {
        @Serializable
        data class SegmentBase(
            @SerialName("index_range")
            val indexRange: String = "", // 944-1107
            @SerialName("initialization")
            val initialization: String = "" // 0-943
        )
    }

    @Serializable
    data class Dolby(
        @SerialName("type")
        val type: Int = 0 // 0
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
        val frameRate: String = "", // 23.810
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
    ) {
        @Serializable
        data class SegmentBase(
            @SerialName("index_range")
            val indexRange: String = "", // 1007-1170
            @SerialName("initialization")
            val initialization: String = "" // 0-1006
        )

        /**
         * 判断视频编码
         */
        fun videoEncode() {
            when (codecid) {
                VideoEncode.AV1.codecid -> {}
                VideoEncodingFormatHEVC -> {}
                VideoEncodingFormatAV1 -> {}
                else -> Timber.d("未知编码格式: codecid=$codecid,codecs=$codecs")
            }
        }

    }
}

/**
 * 7  AVC 8K 视频不支持该格式
 * 12 HEVC
 * 13 AV1
 */
enum class VideoEncode(val codecid: Int){
    AVC(7),
    HEVC(12),
    AV1(13)
}
const val VideoEncodingFormatAVC = 7
const val VideoEncodingFormatHEVC = 12
const val VideoEncodingFormatAV1 = 13
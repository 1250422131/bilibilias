package com.imcys.bilibilias.common.base.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * @author:imcys
 * @create: 2022-12-06 16:59
 * @Description: dash类型视频返回数据
 * ![DASH格式](https://github.com/SocialSisterYi/bilibili-API-collect/blob/master/docs/video/videostream_url.md#dash%E6%A0%BC%E5%BC%8F)
 */
@Serializable
data class DashVideoPlayBean(
    @SerialName("accept_description")
    val acceptDescription: List<String> = listOf(),
    @SerialName("accept_format")
    val acceptFormat: String = "", // hdflv2,hdflv2,flv_p60,flv,flv720_p60,flv480,mp4
    @SerialName("accept_quality")
    val acceptQuality: List<Int> = listOf(),
    @SerialName("dash")
    val dash: Dash = Dash(),
    @SerialName("format")
    val format: String = "", // flv720_p60
    @SerialName("from")
    val from: String = "", // local
    @SerialName("last_play_cid")
    val lastPlayCid: Int = 0, // 0
    @SerialName("last_play_time")
    val lastPlayTime: Int = 0, // 0
    @SerialName("message")
    val message: String = "",
    @SerialName("quality")
    val quality: Int = 0, // 64
    @SerialName("result")
    val result: String = "", // suee
    @SerialName("seek_param")
    val seekParam: String = "", // start
    @SerialName("seek_type")
    val seekType: String = "", // offset
    @SerialName("support_formats")
    val supportFormats: List<SupportFormat> = listOf(),
    @SerialName("timelength")
    val timelength: Int = 0, // 346410
    @SerialName("video_codecid")
    val videoCodecid: Int = 0 // 7
) {
    @Serializable
    data class Dash(
        @SerialName("audio")
        val audio: List<Audio> = listOf(),
        @SerialName("dolby")
        val dolby: Dolby = Dolby(),
        @SerialName("duration")
        val duration: Int = 0, // 347
        @SerialName("flac")
        val flac: Dolby.DolbyAudio = Dolby.DolbyAudio(), // null
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
            val bandwidth: Int = 0, // 329376
            @SerialName("base_url")
            val baseUrl: String = "", // https://xy125x75x230x185xy.mcdn.bilivideo.cn:4483/upgcxcode/65/46/244954665/244954665_f9-1-30280.m4s?e=ig8euxZM2rNcNbdlhoNvNC8BqJIzNbfqXBvEqxTEto8BTrNvN0GvT90W5JZMkX_YN0MvXg8gNEV4NC8xNEV4N03eN0B5tZlqNxTEto8BTrNvNeZVuJ10Kj_g2UB02J0mN0B5tZlqNCNEto8BTrNvNC7MTX502C8f2jmMQJ6mqF2fka1mqx6gqj0eN0B599M=&uipk=5&nbs=1&deadline=1674133321&gen=playurlv2&os=mcdn&oi=606633952&trid=000077eca41ddc4a4dc6926e971dfacc597cu&mid=293793435&platform=pc&upsig=e983f8b4dc35aa8469dc0742d0371e19&uparams=e,uipk,nbs,deadline,gen,os,oi,trid,mid,platform&mcdnid=1002708&bvc=vod&nettype=0&orderid=0,3&buvid=EC1BD8EA-88F6-4951-BF27-2CFE3450C78F167646infoc&build=0&agrr=0&bw=41220&logo=A0000001
            @SerialName("codecid")
            val codecid: Int = 0, // 0
            @SerialName("codecs")
            val codecs: String = "", // mp4a.40.2
            @SerialName("frame_rate")
            val frameRate: String = "",
            @SerialName("height")
            val height: Int = 0, // 0
            @SerialName("id")
            val id: Int = 0, // 30280
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
                val indexRange: String = "", // 908-1779
                @SerialName("initialization")
                val initialization: String = "" // 0-907
            )
        }

        @Serializable
        data class Dolby(
            @SerialName("audio")
            val audio: List<DolbyAudio> = listOf(), // null
            @SerialName("type")
            val type: Int = 0 // 0
        ) {
            @Serializable
            data class DolbyAudio(
                val display: Boolean = false,
                val audio: List<Audio> = listOf()
            )
        }

        @Serializable
        data class Video(
            @SerialName("backup_url")
            val backupUrl: List<String> = listOf(),
            @SerialName("bandwidth")
            val bandwidth: Int = 0, // 13799375
            @SerialName("base_url")
            val baseUrl: String = "", // https://cn-jxjj-ct-01-01.bilivideo.com/upgcxcode/65/46/244954665/244954665_f9-1-30125.m4s?e=ig8euxZM2rNcNbdlhoNvNC8BqJIzNbfqXBvEqxTEto8BTrNvN0GvT90W5JZMkX_YN0MvXg8gNEV4NC8xNEV4N03eN0B5tZlqNxTEto8BTrNvNeZVuJ10Kj_g2UB02J0mN0B5tZlqNCNEto8BTrNvNC7MTX502C8f2jmMQJ6mqF2fka1mqx6gqj0eN0B599M=&uipk=5&nbs=1&deadline=1674133321&gen=playurlv2&os=bcache&oi=606633952&trid=000077eca41ddc4a4dc6926e971dfacc597cu&mid=293793435&platform=pc&upsig=f144133cabdfca56ae3cb14e208aebe1&uparams=e,uipk,nbs,deadline,gen,os,oi,trid,mid,platform&cdnid=4261&bvc=vod&nettype=0&orderid=0,3&buvid=EC1BD8EA-88F6-4951-BF27-2CFE3450C78F167646infoc&build=0&agrr=0&bw=1726751&logo=80000000
            @SerialName("codecid")
            val codecid: Int = 0, // 12
            @SerialName("codecs")
            val codecs: String = "", // hev1.2.4.L156.90
            @SerialName("frame_rate")
            val frameRate: String = "", // 125
            @SerialName("height")
            val height: Int = 0, // 1920
            @SerialName("id")
            val id: Int = 0, // 125
            @SerialName("mime_type")
            val mimeType: String = "", // video/mp4
            @SerialName("sar")
            val sar: String = "", // 1:1
            @SerialName("segment_base")
            val segmentBase: SegmentBase = SegmentBase(),
            @SerialName("start_with_sap")
            val startWithSap: Int = 0, // 1
            @SerialName("width")
            val width: Int = 0 // 3840
        ) {
            @Serializable
            data class SegmentBase(
                @SerialName("index_range")
                val indexRange: String = "", // 1067-1926
                @SerialName("initialization")
                val initialization: String = "" // 0-1066
            )
        }
    }

    @Serializable
    data class SupportFormat(
        @SerialName("codecs")
        val codecs: List<String> = listOf(),
        @SerialName("display_desc")
        val displayDesc: String = "", // HDR
        @SerialName("format")
        val format: String = "", // hdflv2
        @SerialName("new_description")
        val newDescription: String = "", // HDR 真彩
        @SerialName("quality")
        val quality: Int = 0, // 125
        @SerialName("superscript")
        val superscript: String = ""
    )
}

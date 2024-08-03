package com.imcys.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * 番剧播放
 * ![获取番剧视频流URL](https://github.com/SocialSisterYi/bilibili-API-collect/blob/master/docs/bangumi/videostream_url.md#%E8%8E%B7%E5%8F%96%E7%95%AA%E5%89%A7%E8%A7%86%E9%A2%91%E6%B5%81url)
 */
@Serializable
data class PgcPlayUrl(
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
        @SerialName("code")
        val code: Int = 0,
        @SerialName("durl")
        val durl: List<Durl> = listOf(),
        @SerialName("durls")
        val durls: List<Durls> = listOf(),
        @SerialName("fnval")
        val fnval: Int = 0,
        @SerialName("fnver")
        val fnver: Int = 0,
        @SerialName("format")
        val format: String = "",
        @SerialName("is_drm")
        val isDrm: Boolean = false,
        @SerialName("quality")
        val quality: Int = 0,
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
    ) {
        @Serializable
        data class Durls(
            @SerialName("durl")
            val durl: List<Durl> = listOf(),
            @SerialName("quality")
            val quality: Int = 0
        )
    }
}

package com.imcys.bilibilias.network.model.video


import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class BILIVideoDurl(
    @SerialName("ahead")
    val ahead: String?,
    @SerialName("backup_url")
    val backupUrl: List<String> = emptyList(),
    @SerialName("length")
    val length: Long,
    @SerialName("md5")
    val md5: String?,
    @SerialName("order")
    val order: Long,
    @SerialName("size")
    val size: Long,
    @SerialName("url")
    val url: String,
    @SerialName("vhead")
    val vhead: String?
)

@Serializable
data class BILIVideoDurls(
    val durl: List<BILIVideoDurl>,
    val quality: Long
)


@Serializable
data class BILIVideoSupportFormat(
    @SerialName("attribute")
    val attribute: Long = 0,
    @SerialName("codecs")
    val codecs: List<String> = emptyList(),
    @SerialName("description")
    val description: String = "",
    @SerialName("display_desc")
    val displayDesc: String,
    @SerialName("format")
    val format: String,
    @SerialName("has_preview")
    val hasPreview: Boolean = false,
    @SerialName("need_login")
    val needLogin: Boolean = false,
    @SerialName("need_vip")
    val needVip: Boolean = false,
    @SerialName("new_description")
    val newDescription: String,
    @SerialName("quality")
    val quality: Long,
    @SerialName("sub_description")
    val subDescription: String = "",
    @SerialName("superscript")
    val superscript: String = ""
)


@Serializable
data class BILIVideoDash(
    @SerialName("audio")
    val audio: MutableList<Audio> = mutableListOf(),
    @SerialName("dolby")
    val dolby: Dolby?,
    @SerialName("flac")
    val flac: Flac?,
    @SerialName("duration")
    val duration: Long,
    @SerialName("minBufferTime")
    val minBufferTime: Double?,
    @SerialName("video")
    val video: List<Video>
) {
    @Serializable
    data class Audio(
        @SerialName("backupUrl")
        val backupUrl: List<String> = emptyList(),
        @SerialName("backup_url")
        val backup_url: List<String> = emptyList(),
        @SerialName("bandwidth")
        val bandwidth: Long,
        @SerialName("base_url")
        val base_url: String?,
        @SerialName("baseUrl")
        val baseUrl: String?,
        @SerialName("codecid")
        val codecid: Long?,
        @SerialName("codecs")
        val codecs: String,
        @SerialName("frame_rate")
        val frame_rate: String?,
        @SerialName("frameRate")
        val frameRate: String?,
        @SerialName("height")
        val height: Long?,
        @SerialName("id")
        val id: Long,
        @SerialName("md5")
        val md5: String?,
        @SerialName("mimeType")
        val mimeType: String?,
        @SerialName("mime_type")
        val mime_type: String,
        @SerialName("sar")
        val sar: String?,
        @SerialName("segment_base")
        val segment_base: SegmentBase?,
        @SerialName("SegmentBase")
        val segmentBase: SegmentBase?,
        @SerialName("size")
        val size: Long = 0,
        @SerialName("startWithSAP")
        val startWithSAP: Long = 0,
        @SerialName("start_with_sap")
        val startWithSap: Long = 0,
        @SerialName("width")
        val width: Long?
    ) {
        val finalUrl =
            base_url ?: baseUrl ?: (backup_url.firstOrNull() ?: backupUrl.firstOrNull() ?: "")
    }


    @Serializable
    data class Flac(
        @SerialName("audio")
        val audio: Audio?,
        @SerialName("display")
        val display: Boolean
    )

    @Serializable
    data class Dolby(
        @SerialName("audio")
        val audio: List<Audio>?,
//        @SerialName("type")
//        val type: Long = 0
    )

    @Serializable
    data class Video(
        @SerialName("backupUrl")
        val backupUrl: List<String> = emptyList(),
        @SerialName("backup_url")
        val backup_url: List<String>,
        @SerialName("bandwidth")
        val bandwidth: Long,
        @SerialName("base_url")
        val base_url: String?,
        @SerialName("baseUrl")
        val baseUrl: String?,
        @SerialName("codecid")
        val codecid: Long?,
        @SerialName("codecs")
        val codecs: String,
        @SerialName("frame_rate")
        val frame_rate: String?,
        @SerialName("frameRate")
        val frameRate: String?,
        @SerialName("height")
        val height: Long?,
        @SerialName("id")
        val id: Long,
        @SerialName("md5")
        val md5: String?,
        @SerialName("mimeType")
        val mimeType: String?,
        @SerialName("mime_type")
        val mime_type: String,
        @SerialName("sar")
        val sar: String?,
        @SerialName("segment_base")
        val segment_base: SegmentBase?,
        @SerialName("SegmentBase")
        val segmentBase: SegmentBase?,
        @SerialName("size")
        val size: Long = 0,
        @SerialName("startWithSAP")
        val startWithSAP: Long = 0,
        @SerialName("start_with_sap")
        val startWithSap: Long = 0,
        @SerialName("width")
        val width: Long?
    ){
        val finalUrl =
            base_url ?: baseUrl ?: (backup_url.firstOrNull() ?: backupUrl.firstOrNull() ?: "")
    }

    @Serializable
    data class SegmentBase(
        @SerialName("index_range")
        val indexRange: String?,
        @SerialName("initialization")
        val initialization: String?
    )
}

sealed interface SelectEpisodeType {
    data class BVID(val bvid: String) : SelectEpisodeType
    data class AID(val aid: Long) : SelectEpisodeType
    data class EPID(val epid: Long) : SelectEpisodeType
    data class SSID(val ssid: Long) : SelectEpisodeType
}

fun convertAudioQualityIdValue(id: Long): String =
    mapOf(
        30216L to "64K",
        30232L to "132K",
        30280L to "192K",
        30250L to "杜比全景声",
        30251L to "Hi-Res无损"
    )[id] ?: id.toString()


fun convertVideoQualityIdValue(id: Long): String =
    mapOf(
        6L to "240P 极速",
        16L to "360P 流畅",
        32L to "480P 清晰",
        64L to "720P 高清",
        74L to "720P60 高帧率",
        80L to "1080P 高清",
        100L to "智能修复",
        112L to "1080P+ 高码率",
        116L to "1080P60 高帧率",
        120L to "4K 超清",
        125L to "HDR 真彩色",
        126L to "杜比视界",
        127L to "8K 超高清"
    )[id] ?: id.toString()
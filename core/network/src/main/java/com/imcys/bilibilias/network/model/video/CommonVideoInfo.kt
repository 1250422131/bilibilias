package com.imcys.bilibilias.network.model.video


import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class BILIVideoDurl(
    @SerialName("ahead")
    val ahead: String,
    @SerialName("backup_url")
    val backupUrl: List<String>,
    @SerialName("length")
    val length: Long,
    @SerialName("md5")
    val md5: String,
    @SerialName("order")
    val order: Long,
    @SerialName("size")
    val size: Long,
    @SerialName("url")
    val url: String,
    @SerialName("vhead")
    val vhead: String
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
    val codecs: List<String>,
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
    val audio: List<Audio>,
    @SerialName("dolby")
    val dolby: Dolby,
    @SerialName("duration")
    val duration: Long,
    @SerialName("minBufferTime")
    val minBufferTime: Double,
    @SerialName("video")
    val video: List<Video>
) {
    @Serializable
    data class Audio(
        @SerialName("backupUrl")
        val backupUrl: List<String>,
        @SerialName("backup_url")
        val backup_url: List<String>,
        @SerialName("bandwidth")
        val bandwidth: Long,
        @SerialName("base_url")
        val base_url: String,
        @SerialName("baseUrl")
        val baseUrl: String,
        @SerialName("codecid")
        val codecid: Long,
        @SerialName("codecs")
        val codecs: String,
        @SerialName("frame_rate")
        val frame_rate: String,
        @SerialName("frameRate")
        val frameRate: String,
        @SerialName("height")
        val height: Long,
        @SerialName("id")
        val id: Long,
        @SerialName("md5")
        val md5: String?,
        @SerialName("mimeType")
        val mimeType: String,
        @SerialName("mime_type")
        val mime_type: String,
        @SerialName("sar")
        val sar: String,
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
        val width: Long
    )

    @Serializable
    data class Dolby(
        @SerialName("audio")
        val audio: List<String>? = null,
        @SerialName("type")
        val type: Long
    )

    @Serializable
    data class Video(
        @SerialName("backupUrl")
        val backupUrl: List<String>,
        @SerialName("backup_url")
        val backup_url: List<String>,
        @SerialName("bandwidth")
        val bandwidth: Long,
        @SerialName("base_url")
        val base_url: String,
        @SerialName("baseUrl")
        val baseUrl: String,
        @SerialName("codecid")
        val codecid: Long,
        @SerialName("codecs")
        val codecs: String,
        @SerialName("frame_rate")
        val frame_rate: String,
        @SerialName("frameRate")
        val frameRate: String,
        @SerialName("height")
        val height: Long,
        @SerialName("id")
        val id: Long,
        @SerialName("md5")
        val md5: String?,
        @SerialName("mimeType")
        val mimeType: String,
        @SerialName("mime_type")
        val mime_type: String,
        @SerialName("sar")
        val sar: String,
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
        val width: Long
    )

    @Serializable
    data class SegmentBase(
        @SerialName("index_range")
        val indexRange: String?,
        @SerialName("initialization")
        val initialization: String?
    )
}
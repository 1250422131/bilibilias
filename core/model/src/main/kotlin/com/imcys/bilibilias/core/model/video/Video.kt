package com.imcys.bilibilias.core.model.video

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Video(
    @SerialName("backup_url")
    val backupUrl: List<String> = listOf(),
    @SerialName("bandwidth")
    val bandwidth: Int = 0,
    @SerialName("base_url")
    val baseUrl: String = "",
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
    val width: Int = 0,
)

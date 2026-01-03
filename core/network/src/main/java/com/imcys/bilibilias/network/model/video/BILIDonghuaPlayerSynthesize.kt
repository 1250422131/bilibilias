package com.imcys.bilibilias.network.model.video

import kotlinx.serialization.SerialName

/**
 * 统筹后的番剧播放信息
 */
data class BILIDonghuaPlayerSynthesize(
    @SerialName("accept_description")
    val acceptDescription: List<String> = emptyList(),
    @SerialName("accept_format")
    val acceptFormat: String,
    @SerialName("accept_quality")
    val acceptQuality: List<Long>,
    @SerialName("format")
    val format: String,
    @SerialName("support_formats")
    val supportFormats: List<BILIVideoSupportFormat>,
    @SerialName("dash")
    val dash: BILIVideoDash? = null,
    @SerialName("durl")
    val durl: List<BILIVideoDurl>?,
    @SerialName("durls")
    val durls: List<BILIVideoDurls>?,
    @SerialName("timelength")
    val timelength: Long,
    @SerialName("type")
    val type: String,
    @SerialName("quality")
    val quality: Long,
    @SerialName("video_codecid")
    val videoCodecid: Long,
    @SerialName("is_preview")
    val isPreview: Boolean,
)
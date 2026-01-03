package com.imcys.bilibilias.network.model.video


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Anime
 * 番剧/动画
 * 为了更加全面，我们这里叫Donghua->动画
 */
@Serializable
data class BILIDonghuaPlayerInfo(
    @SerialName("accept_description")
    val acceptDescription: List<String> = emptyList(),
    @SerialName("accept_format")
    val acceptFormat: String,
    @SerialName("accept_quality")
    val acceptQuality: List<Long>,
    @SerialName("bp")
    val bp: Long?,
    @SerialName("code")
    val code: Long?,
    @SerialName("dash")
    val dash: BILIVideoDash? = null,
    @SerialName("durl")
    val durl: List<BILIVideoDurl>?,
    @SerialName("durls")
    val durls: List<BILIVideoDurls>?,
    @SerialName("error_code")
    val errorCode: Long?,
    @SerialName("fnval")
    val fnval: Long?,
    @SerialName("fnver")
    val fnver: Long?,
    @SerialName("format")
    val format: String,
    @SerialName("from")
    val from: String?,
    @SerialName("has_paid")
    val hasPaid: Boolean?,
    @SerialName("is_drm")
    val isDrm: Boolean?,
    @SerialName("is_preview")
    val isPreview: Long?,
    @SerialName("message")
    val message: String?,
    @SerialName("no_rexcode")
    val noRexcode: Long?,
    @SerialName("quality")
    val quality: Long,
    @SerialName("result")
    val result: String?,
    @SerialName("seek_param")
    val seekParam: String?,
    @SerialName("seek_type")
    val seekType: String?,
    @SerialName("status")
    val status: Long?,
    @SerialName("support_formats")
    val supportFormats: List<BILIVideoSupportFormat>,
    @SerialName("timelength")
    val timelength: Long,
    @SerialName("type")
    val type: String,
    @SerialName("video_codecid")
    val videoCodecid: Long,
    @SerialName("video_project")
    val videoProject: Boolean?
)

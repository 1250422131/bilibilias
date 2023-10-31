package com.imcys.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
@Serializable
data class VideoStreamingURL(
    @SerialName("accept_description")
    val acceptDescription: List<String> = listOf(),
    @SerialName("accept_format")
    val acceptFormat: String = "",
    @SerialName("accept_quality")
    val acceptQuality: List<Int> = listOf(),
    @SerialName("format")
    val format: String = "",
    @SerialName("from")
    val from: String = "",
    @SerialName("last_play_cid")
    val lastPlayCid: Int = 0,
    @SerialName("last_play_time")
    val lastPlayTime: Int = 0,
    @SerialName("message")
    val message: String = "",
    @SerialName("quality")
    val quality: Int = 0,
    @SerialName("result")
    val result: String = "",
    @SerialName("seek_param")
    val seekParam: String = "",
    @SerialName("seek_type")
    val seekType: String = "",
    @SerialName("support_formats")
    val supportFormats: List<SupportFormat> = listOf(),
    @SerialName("timelength")
    val timelength: Int = 0,
    @SerialName("video_codecid")
    val videoCodecid: Int = 0,
    @SerialName("durl")
    val durl: List<Durl>? = null,
    @SerialName("dash")
    val dash: com.imcys.model.Dash? = null,
)

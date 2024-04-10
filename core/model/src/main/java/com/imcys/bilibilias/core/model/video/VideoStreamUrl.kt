package com.imcys.bilibilias.core.model.video

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class VideoStreamUrl(
    @SerialName("accept_description")
    val acceptDescription: List<String> = listOf(),
    @SerialName("accept_format")
    val acceptFormat: String = "",
    @SerialName("accept_quality")
    val acceptQuality: List<Int> = listOf(),
    @SerialName("dash")
    val dash: Dash = Dash(),
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
    @SerialName("volume")
    val volume: Volume = Volume()
) {
    @Serializable
    data class Volume(
        @SerialName("measured_i")
        val measuredI: Double = 0.0,
        @SerialName("measured_lra")
        val measuredLra: Double = 0.0,
        @SerialName("measured_threshold")
        val measuredThreshold: Double = 0.0,
        @SerialName("measured_tp")
        val measuredTp: Double = 0.0,
        @SerialName("target_i")
        val targetI: Int = 0,
        @SerialName("target_offset")
        val targetOffset: Double = 0.0,
        @SerialName("target_tp")
        val targetTp: Int = 0
    )
}

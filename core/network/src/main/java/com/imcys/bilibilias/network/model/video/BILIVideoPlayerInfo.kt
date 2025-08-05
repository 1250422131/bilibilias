package com.imcys.bilibilias.network.model.video

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName


@Serializable
data class BILIVideoPlayerInfo(
    @SerialName("accept_description")
    val acceptDescription: List<String> = emptyList(),
    @SerialName("accept_format")
    val acceptFormat: String,
    @SerialName("accept_quality")
    val acceptQuality: List<Long>,
    val dash: BILIVideoDash?,
    @SerialName("durl")
    val durl: List<BILIVideoDurl>?,
    @SerialName("durls")
    val durls: List<BILIVideoDurls>?,
    @SerialName("format")
    val format: String,
    @SerialName("from")
    val from: String,
    @SerialName("last_play_cid")
    val lastPlayCid: Long,
    @SerialName("last_play_time")
    val lastPlayTime: Long,
    @SerialName("message")
    val message: String,
    @SerialName("play_conf")
    val playConf: PlayConf,
    @SerialName("quality")
    val quality: Long,
    @SerialName("result")
    val result: String,
    @SerialName("seek_param")
    val seekParam: String,
    @SerialName("seek_type")
    val seekType: String,
    @SerialName("support_formats")
    val supportFormats: List<BILIVideoSupportFormat>,
    @SerialName("timelength")
    val timelength: Long,
    @SerialName("video_codecid")
    val videoCodecid: Long,
) {
    @Serializable
    data class PlayConf(
        @SerialName("is_new_description")
        val isNewDescription: Boolean
    )
}

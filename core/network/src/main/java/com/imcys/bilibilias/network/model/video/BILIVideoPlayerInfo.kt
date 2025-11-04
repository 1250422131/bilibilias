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
    @SerialName("cur_language")
    val curLanguage: String? = "",
    @SerialName("cur_production_type")
    val curProductionType: Int = 0,
    @SerialName("language")
    val language: BILIVideoLanguage? = null,
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


@Serializable
data class BILIVideoLanguage(
    @SerialName("bubble")
    val bubble: Bubble,
    @SerialName("close_toast")
    val closeToast: String,
    @SerialName("default_title")
    val defaultTitle: String,
    @SerialName("items")
    val items: List<BILIVideoLanguageItem>,
    @SerialName("list_title")
    val listTitle: String,
    @SerialName("open_toast")
    val openToast: String,
    @SerialName("support")
    val support: Boolean
) {
    @Serializable
    data class Bubble(
        @SerialName("title")
        val title: String,
        @SerialName("type")
        val type: Int
    )

}

@Serializable
data class BILIVideoLanguageItem(
    @SerialName("lang")
    val lang: String,
    @SerialName("production_type")
    val productionType: Int,
    @SerialName("subtitle_lang")
    val subtitleLang: String,
    @SerialName("title")
    val title: String,
    @SerialName("video_detext")
    val videoDetext: Boolean,
    @SerialName("video_mouth_shape_change")
    val videoMouthShapeChange: Boolean
)
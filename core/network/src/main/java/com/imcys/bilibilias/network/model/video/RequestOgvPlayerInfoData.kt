package com.imcys.bilibilias.network.model.video

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class RequestOgvPlayerInfoData(
    @SerialName("exp_info")
    val expInfo: OgvExpInfo,
    @SerialName("player_param")
    val playerParam: OgvPlayerParam,
    @SerialName("video_param")
    val videoParam: OgvVideoParam,
    @SerialName("video_index")
    val videoIndex: OgvVideoIndex,
    @SerialName("scene")
    val scene: String
)

@Serializable
data class OgvExpInfo(
    @SerialName("device_support_hdr")
    val deviceSupportHdr: Boolean,
    @SerialName("ogv_half_pay")
    val ogvHalfPay: Boolean,
)

@Serializable
data class OgvPlayerParam(
    @SerialName("drm_tech_type")
    val drmTechType: Int,
    @SerialName("fnval")
    val fnval: Int,
    @SerialName("fnver")
    val fnver: Int,
)

@Serializable
data class OgvVideoParam(
    @SerialName("qn")
    val qn: Int,
)

@Serializable
data class OgvVideoIndex(
    @SerialName("bvid")
    val bvid: String? = null,
    @SerialName("cid")
    val cid: Long? = null,
    @SerialName("ogv_episode_id")
    val ogvEpisodeId: Long?,
    @SerialName("ogv_season_id")
    val ogvSeasonId: Long?,
)


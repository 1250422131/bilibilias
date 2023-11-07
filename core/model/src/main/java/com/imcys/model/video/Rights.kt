package com.imcys.model.video

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Rights(
    @SerialName("arc_pay")
    val arcPay: Int = 0,
    @SerialName("autoplay")
    val autoplay: Int = 0,
    @SerialName("bp")
    val bp: Int = 0,
    @SerialName("clean_mode")
    val cleanMode: Int = 0,
    @SerialName("download")
    val download: Int = 0,
    @SerialName("elec")
    val elec: Int = 0,
    @SerialName("free_watch")
    val freeWatch: Int = 0,
    @SerialName("hd5")
    val hd5: Int = 0,
    @SerialName("is_360")
    val is360: Int = 0,
    @SerialName("is_cooperation")
    val isCooperation: Int = 0,
    @SerialName("is_stein_gate")
    val isSteinGate: Int = 0,
    @SerialName("movie")
    val movie: Int = 0,
    @SerialName("no_background")
    val noBackground: Int = 0,
    @SerialName("no_reprint")
    val noReprint: Int = 0,
    @SerialName("no_share")
    val noShare: Int = 0,
    @SerialName("pay")
    val pay: Int = 0,
    @SerialName("ugc_pay")
    val ugcPay: Int = 0,
    @SerialName("ugc_pay_preview")
    val ugcPayPreview: Int = 0
)
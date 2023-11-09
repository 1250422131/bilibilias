package com.imcys.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Archive(
    @SerialName("aid")
    val aid: Int = 0,
    @SerialName("bvid")
    val bvid: String = "",
    @SerialName("ctime")
    val ctime: Int = 0,
    @SerialName("duration")
    val duration: Int = 0,
    @SerialName("interactive_video")
    val interactiveVideo: Boolean = false,
    @SerialName("pic")
    val pic: String = "",
    @SerialName("pubdate")
    val pubdate: Int = 0,
    @SerialName("stat")
    val stat: Stat = Stat(),
    @SerialName("state")
    val state: Int = 0,
    @SerialName("title")
    val title: String = "",
    @SerialName("ugc_pay")
    val ugcPay: Int = 0
) {
    @Serializable
    data class Stat(
        @SerialName("view")
        val view: Int = 0
    )
}
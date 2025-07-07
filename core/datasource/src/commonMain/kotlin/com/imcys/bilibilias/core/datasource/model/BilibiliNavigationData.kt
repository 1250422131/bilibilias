package com.imcys.bilibilias.core.datasource.model


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class BilibiliNavigationData(
    @SerialName("wbi_img")
    val wbiImg: WbiImg = WbiImg()
) {
    @Serializable
    data class WbiImg(
        @SerialName("img_url")
        val imgUrl: String = "",
        @SerialName("sub_url")
        val subUrl: String = ""
    )
}
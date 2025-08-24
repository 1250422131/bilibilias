package com.imcys.bilibilias.core.datasource.model


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class BilibiliNavigationData(
    @SerialName("isLogin")
    val isLogin: Boolean,
    @SerialName("face")
    val face: String? = null,
    @SerialName("mid")
    val mid: Long? = null,
    @SerialName("uname")
    val uname: String? = null,
    @SerialName("pendant")
    val pendant: Pendant? = null,
    @SerialName("wbi_img")
    val wbiImg: WbiImg,
) {
    @Serializable
    data class Pendant(
        @SerialName("pid")
        val pid: Int,
        @SerialName("name")
        val name: String,
        @SerialName("image")
        val image: String,
        @SerialName("image_enhance")
        val imageEnhance: String,
        @SerialName("image_enhance_frame")
        val imageEnhanceFrame: String,
    )

    @Serializable
    data class WbiImg(
        @SerialName("img_url")
        val imgUrl: String,
        @SerialName("sub_url")
        val subUrl: String,
    )
}
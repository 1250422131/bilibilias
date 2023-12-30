package com.imcys.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Label(
    @SerialName("bg_color")
    val bgColor: String = "",
    @SerialName("bg_style")
    val bgStyle: Int = 0,
    @SerialName("border_color")
    val borderColor: String = "",
    @SerialName("img_label_uri_hans")
    val imgLabelUriHans: String = "",
    @SerialName("img_label_uri_hans_static")
    val imgLabelUriHansStatic: String = "",
    @SerialName("img_label_uri_hant")
    val imgLabelUriHant: String = "",
    @SerialName("img_label_uri_hant_static")
    val imgLabelUriHantStatic: String = "",
    @SerialName("label_theme")
    val labelTheme: String = "",
    @SerialName("path")
    val path: String = "",
    @SerialName("text")
    val text: String = "",
    @SerialName("text_color")
    val textColor: String = "",
    @SerialName("use_img_label")
    val useImgLabel: Boolean = false
)
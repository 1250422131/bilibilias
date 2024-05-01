package com.imcys.bilibilias.home.ui.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class VideoCCInfo(
    @SerialName("background_alpha")
    val backgroundAlpha: Double = 0.0,
    @SerialName("background_color")
    val backgroundColor: String = "",
    @SerialName("body")
    val body: List<Body> = listOf(),
    @SerialName("font_color")
    val fontColor: String = "",
    @SerialName("font_size")
    val fontSize: Double = 0.0,
    @SerialName("lang")
    val lang: String = "",
    @SerialName("Stroke")
    val stroke: String = "",
    @SerialName("type")
    val type: String = "",
    @SerialName("version")
    val version: String = ""
) {
    @Serializable
    data class Body(
        @SerialName("content")
        val content: String = "",
        @SerialName("from")
        val from: Double = 0.0,
        @SerialName("location")
        val location: Int = 0,
        @SerialName("music")
        val music: Double = 0.0,
        @SerialName("sid")
        val sid: Int = 0,
        @SerialName("to")
        val to: Double = 0.0
    )
}
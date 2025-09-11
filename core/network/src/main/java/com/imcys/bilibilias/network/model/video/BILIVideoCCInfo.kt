package com.imcys.bilibilias.network.model.video

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName


@Serializable
data class BILIVideoCCInfo(
    @SerialName("background_alpha")
    val backgroundAlpha: Double,
    @SerialName("background_color")
    val backgroundColor: String,
    @SerialName("body")
    val body: List<Body>,
    @SerialName("font_color")
    val fontColor: String,
    @SerialName("font_size")
    val fontSize: Double,
    @SerialName("lang")
    val lang: String,
    @SerialName("Stroke")
    val stroke: String,
    @SerialName("type")
    val type: String,
    @SerialName("version")
    val version: String
) {
    @Serializable
    data class Body(
        @SerialName("content")
        val content: String,
        @SerialName("from")
        val from: Double,
        @SerialName("location")
        val location: Int,
        @SerialName("music")
        val music: Double,
        @SerialName("sid")
        val sid: Int,
        @SerialName("to")
        val to: Double
    )
}


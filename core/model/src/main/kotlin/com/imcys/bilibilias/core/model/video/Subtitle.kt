package com.imcys.bilibilias.core.model.video

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Subtitle(
    @SerialName("allow_submit")
    val allowSubmit: Boolean = false,
    @SerialName("list")
    val list: List<Subtitles> = listOf(),
) {
    @Serializable
    data class Subtitles(
        @SerialName("ai_status")
        val aiStatus: Int = 0,
        @SerialName("ai_type")
        val aiType: Int = 0,
        @SerialName("author")
        val author: Author = Author(),
        @SerialName("id")
        val id: Long = 0,
        @SerialName("id_str")
        val idStr: String = "",
        @SerialName("is_lock")
        val isLock: Boolean = false,
        @SerialName("lan")
        val lan: String = "",
        @SerialName("lan_doc")
        val lanDoc: String = "",
        @SerialName("subtitle_url")
        val subtitleUrl: String = "",
        @SerialName("type")
        val type: Int = 0,
    )
}

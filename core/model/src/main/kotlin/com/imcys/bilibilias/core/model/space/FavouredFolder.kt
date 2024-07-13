package com.imcys.bilibilias.core.model.space

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class FavouredFolder(
    @SerialName("count")
    val count: Int = 0,
    @SerialName("list")
    val list: List<Folder> = listOf(),
) {
    @Serializable
    data class Folder(
        @SerialName("attr")
        val attr: Int = 0,
        @SerialName("fav_state")
        val favState: Int = 0,
        @SerialName("fid")
        val fid: Int = 0,
        @SerialName("id")
        val id: Long = 0,
        @SerialName("media_count")
        val mediaCount: Int = 0,
        @SerialName("mid")
        val mid: Long = 0,
        @SerialName("title")
        val title: String = ""
    )
}

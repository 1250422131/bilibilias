package com.imcys.model.space

import com.imcys.model.Page
import com.imcys.model.video.Archive
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SeasonsArchivesList(
    @SerialName("aids")
    val aids: List<Int>,
    @SerialName("archives")
    val archives: List<Archive> = emptyList(),
    @SerialName("meta")
    val meta: Meta = Meta(),
    @SerialName("page")
    val page: Page = Page()
)

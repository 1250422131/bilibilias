package com.imcys.model.space

import com.imcys.model.Page
import com.imcys.model.video.Archive
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ChannelCollectionDetail(
    @SerialName("aids")
    val aids: List<Int> = listOf(),
    @SerialName("archives")
    val archives: List<Archive> = listOf(),
    @SerialName("meta")
    val meta: Meta = Meta(),
    @SerialName("page")
    val page: Page = Page()
)

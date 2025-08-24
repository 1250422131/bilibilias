package com.imcys.bilibilias.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class BILISpaceArchiveModel(
    @SerialName("list")
    val list: List<Item>,
    val page: Page
) {
    @Serializable
    data class Item(
        val aid: Long,
        val attribute: Long,
        val author: String,
        val bvid: String,
        val comment: Long,
        val seasonId: Long,
        val title: String,
        val length: String,
        val play: Long,
        val danmu: Long,
        val description: String,
        val pic: String,
    )

    @Serializable
    data class Page(
        val count: Long,
        val hasNext: Boolean,
        val hasPrev: Boolean
    )
}
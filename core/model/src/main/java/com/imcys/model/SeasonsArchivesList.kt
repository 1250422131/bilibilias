package com.imcys.model


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SeasonsArchivesList(
    @SerialName("aids")
    val aids: List<Int> = listOf(),
    @SerialName("archives")
    val archives: List<Archive> = listOf(),
    @SerialName("meta")
    val meta: Meta = Meta(),
    @SerialName("page")
    val page: Page = Page()
) {


    @Serializable
    data class Meta(
        @SerialName("category")
        val category: Int = 0,
        @SerialName("cover")
        val cover: String = "",
        @SerialName("description")
        val description: String = "",
        @SerialName("mid")
        val mid: Int = 0,
        @SerialName("name")
        val name: String = "",
        @SerialName("ptime")
        val ptime: Int = 0,
        @SerialName("season_id")
        val seasonId: Int = 0,
        @SerialName("total")
        val total: Int = 0
    )

    @Serializable
    data class Page(
        @SerialName("page_num")
        val pageNum: Int = 0,
        @SerialName("page_size")
        val pageSize: Int = 0,
        @SerialName("total")
        val total: Int = 0
    )
}
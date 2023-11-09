package com.imcys.model


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 *
 * | seasons_list | arr  |   新版内容   |      |
 * | series_list | arr  |   旧版内容   |      |
 */
@Serializable
data class SeasonsSeriesList(
    @SerialName("items_lists")
    val itemsLists: ItemsLists = ItemsLists()
) {
    @Serializable
    data class ItemsLists(
        @SerialName("page")
        val page: Page = Page(),
        @SerialName("seasons_list")
        val seasonsList: List<Seasons> = listOf(),
        @SerialName("series_list")
        val seriesList: List<Series> = listOf()
    ) {
        @Serializable
        data class Page(
            @SerialName("page_num")
            val pageNum: Int = 0,
            @SerialName("page_size")
            val pageSize: Int = 0,
            @SerialName("total")
            val total: Int = 0
        )

        @Serializable
        data class Seasons(
            @SerialName("archives")
            val archives: List<Archive> = listOf(),
            @SerialName("meta")
            val meta: Meta = Meta(),
            @SerialName("recent_aids")
            val recentAids: List<Int> = listOf()
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
        }

        @Serializable
        data class Series(
            @SerialName("archives")
            val archives: List<Archive> = listOf(),
            @SerialName("meta")
            val meta: Meta = Meta(),
            @SerialName("recent_aids")
            val recentAids: List<Int> = listOf()
        ) {
            @Serializable
            data class Meta(
                @SerialName("category")
                val category: Int = 0,
                @SerialName("cover")
                val cover: String = "",
                @SerialName("creator")
                val creator: String = "",
                @SerialName("ctime")
                val ctime: Int = 0,
                @SerialName("description")
                val description: String = "",
                @SerialName("keywords")
                val keywords: List<String> = listOf(),
                @SerialName("last_update_ts")
                val lastUpdateTs: Int = 0,
                @SerialName("mid")
                val mid: Int = 0,
                @SerialName("mtime")
                val mtime: Int = 0,
                @SerialName("name")
                val name: String = "",
                @SerialName("raw_keywords")
                val rawKeywords: String = "",
                @SerialName("series_id")
                val seriesId: Int = 0,
                @SerialName("state")
                val state: Int = 0,
                @SerialName("total")
                val total: Int = 0
            )
        }
    }
}

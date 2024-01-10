package com.imcys.model.space

import com.imcys.model.Page
import com.imcys.model.video.Archive
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SeasonsSeriesList(
    @SerialName("items_lists")
    val itemsLists: ItemsLists = ItemsLists()
) {
    @Serializable
    data class ItemsLists
    constructor(
        @SerialName("page")
        val page: Page = Page(),
        @SerialName("series_list")
        val seriesList: List<SeasonsSeries> = emptyList(),
        @SerialName("seasons_list")
        val seasonsList: List<SeasonsSeries> = emptyList()
    ) {
        @Serializable
        data class SeasonsSeries(
            @SerialName("archives")
            val archives: List<Archive> = emptyList(),
            @SerialName("meta")
            val meta: Meta = Meta(),
            @SerialName("recent_aids")
            val recentAids: List<Long> = emptyList()
        )
    }
}

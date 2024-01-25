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
    data class ItemsLists(
        @SerialName("page")
        val page: Page = Page(),
        // https://api.bilibili.com/x/polymer/web-space/seasons_archives_list?season_id=587216&sort_reverse=false&page_num=1&page_size=30&web_location=333.999&w_rid=23b2bc41f3729c798611c61e2b1a68d4&wts=1706021613
        @SerialName("seasons_list")
        val seasonsList: List<SeasonsSeries> = emptyList(),
        @SerialName("series_list")
        // 这个是在进入up空间投稿下面一行的
        // https://api.bilibili.com/x/polymer/space/seasons_archives_list?mid=37737161&sort_reverse=false&season_id=1227671&page_num=1&page_size=30
        // https://api.bilibili.com/x/polymer/space/seasons_archives_list?mid=37737161&sort_reverse=false&season_id=3908327&page_num=1&page_size=30
        //         api.bilibili.com/x/polymer/web-space/home/seasons_series?mid=37737161&page_num=1&page_size=10
        //  getChannelsWithArchives: function() {
        //                    return Object(A.a)({
        //                        url: "//api.bilibili.com/x/polymer/web-space/home/seasons_series",
        // /x/series/series?series_id=2800548
        //
        // /x/polymer/web-space/seasons_series_list?mid=37737161&page_num=1&page_size=18&web_location=333.999&w_rid=17f62ea239fc9045873a6aeb660a1638&wts=1706021374
        val seriesList: List<SeasonsSeries> = emptyList(),
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

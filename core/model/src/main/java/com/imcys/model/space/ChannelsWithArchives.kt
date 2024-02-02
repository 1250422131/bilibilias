package com.imcys.model.space

import com.imcys.model.Page
import com.imcys.model.video.Archive
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonNames

// https://api.bilibili.com/x/polymer/web-space/seasons_archives_list?season_id=587216&sort_reverse=false&page_num=1&page_size=30&web_location=333.999&w_rid=23b2bc41f3729c798611c61e2b1a68d4&wts=1706021613

// 这个是在进入up空间投稿下面一行的
// https://api.bilibili.com/x/polymer/space/seasons_archives_list?mid=37737161&sort_reverse=false&season_id=1227671&page_num=1&page_size=30
// https://api.bilibili.com/x/polymer/space/seasons_archives_list?mid=37737161&sort_reverse=false&season_id=3908327&page_num=1&page_size=30
//         api.bilibili.com/x/polymer/web-space/home/seasons_series?mid=37737161&page_num=1&page_size=10
//                        url: "//api.bilibili.com/x/polymer/web-space/home/seasons_series",
// /x/series/series?series_id=2800548
//
// /x/polymer/web-space/seasons_series_list?mid=37737161&page_num=1&page_size=18&web_location=333.999&w_rid=17f62ea239fc9045873a6aeb660a1638&wts=1706021374

@Serializable
data class ChannelsWithArchives(
    @SerialName("items_lists")
    val items: ItemsLists = ItemsLists()
) {
    @Serializable
    data class ItemsLists(
        @SerialName("page")
        val page: Page = Page(),
        // api.bilibili.com/x/polymer/web-space/seasons_archives_list?mid=2379178&season_id=993775&sort_reverse=false&page_num=1&page_size=30
        @SerialName("seasons_list")
        val seasons: List<ChannelItem> = listOf(),
        // api.bilibili.com/x/series/series?series_id=957053
        @SerialName("series_list")
        val series: List<ChannelItem> = listOf()
    ) {
        @Serializable
        data class ChannelItem(
            @SerialName("archives")
            val archives: List<Archive> = listOf(),
            @SerialName("meta")
            val meta: Meta = Meta(),
            @SerialName("recent_aids")
            val recentAids: List<Long> = listOf()
        )

        @OptIn(ExperimentalSerializationApi::class)
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
            val mid: Long = 0,
            @SerialName("mtime")
            val mtime: Int = 0,
            @SerialName("name")
            val name: String = "",
            @SerialName("raw_keywords")
            val rawKeywords: String = "",
            @JsonNames("series_id", "season_id")
            val id: Long = 0,
            @SerialName("state")
            val state: Int = 0,
            @SerialName("total")
            val total: Int = 0
        )
    }
}

package com.imcys.player.state

import com.imcys.model.space.SeasonsSeriesList
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList

data class SeriesArchive(
    val aid: Long = 0,
    val bvid: String = "",
    val cid: Long = 0,
    val title: String = "",
)

internal fun SeasonsSeriesList.ItemsLists.SeasonsSeries.mapToSeriesArchive(): ImmutableList<SeriesArchive> {
    return archives.map {
        SeriesArchive(
            aid = it.aid,
            bvid = it.bvid,
            cid = it.cid,
            title = it.title,
        )
    }.toImmutableList()
}

//internal fun VideoDetails.mapToSeriesArchive(): ImmutableList<SeriesArchive> {
//    return persistentListOf(SeriesArchive(aid, bvid, cid, title))
//}
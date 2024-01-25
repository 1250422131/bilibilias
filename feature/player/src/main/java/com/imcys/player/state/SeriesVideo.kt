package com.imcys.player.state

import com.imcys.model.PgcViewSeason
import com.imcys.model.ViewDetail
import com.imcys.model.space.SeasonsArchives
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList

data class SeriesVideo(
    val aid: Long = 0,
    val bvid: String = "",
    val cid: Long = 0,
    val title: String = "",
)

internal fun ViewDetail.mapToSeriesVideo(): ImmutableList<SeriesVideo> {
    return pages.map {
        SeriesVideo(aid, bvid, it.cid, it.part)
    }.toImmutableList()
}

internal fun ImmutableList<SeasonsArchives>.mapToSeriesVideo(): ImmutableList<SeriesVideo> {
    return this.flatMap { series ->
        series.archives.map {
            SeriesVideo(it.aid, it.bvid, it.cid, it.title)
        }
    }.toImmutableList()
}

internal fun PgcViewSeason.mapToSeriesVideo(): ImmutableList<SeriesVideo> {
    return episodes.map {
        SeriesVideo(
            it.aid,
            it.bvid,
            it.cid,
            it.title
        )
    }.toImmutableList()
}
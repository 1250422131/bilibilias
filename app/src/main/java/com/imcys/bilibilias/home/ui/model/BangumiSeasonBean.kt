package com.imcys.bilibilias.home.ui.model

import kotlinx.serialization.Serializable

/**
 * 番剧剧集明细
 */
@Serializable
data class BangumiSeasonBean(
    val code: Int = 0,
    val message: String = "",
    val result: ResultBean = ResultBean(),
) {
    @Serializable
    data class ResultBean(
        val activity: ActivityBean = ActivityBean(),
        val cover: String = "",
        val season_id: Int = 0,
        val section: List<SectionBean> = emptyList(),
        val status: Int = 0,
        val title: String = "",
        val total: Int = 0,
        val type: Int = 0,
        val episodes: List<EpisodesBean> = emptyList(),
    ) {
        @Serializable
        data class ActivityBean(
            val id: Int = 0,
            val title: String = "",
        )

        @Serializable
        data class EpisodesBean(
            var checkState: Int = 0,
            var selected: Int = 0,
            val aid: Long = 0,
            val badge: String = "",
            val bvid: String = "",
            val cid: Long = 0,
            val cover: String = "",
            val dimension: DimensionBean = DimensionBean(),
            val duration: Int = 0,
            val from: String = "",
            val id: Long = 0,
            val long_title: String = "",
            val pv: Int = 0,
            val share_copy: String = "",
            val share_url: String = "",
            val status: Int = 0,
            val title: String = "",
        ) {
            @Serializable
            data class DimensionBean(
                val height: Int = 0,
                val width: Int = 0,
            )
        }

        @Serializable
        data class SectionBean(
            val attr: Int = 0,
            val id: Int = 0,
            val title: String = "",
            val type: Int = 0,
            val episodes: List<EpisodesBeanX> = emptyList(),
        ) {
            @Serializable
            data class EpisodesBeanX(
                val bvid: String = "",
                val cid: Long = 0,
                val cover: String = "",
                val duration: Int = 0,
                val from: String = "",
                val id: Int = 0,
                val long_title: String = "",
                val pv: Int = 0,
                val status: Int = 0,
                val title: String = "",
            )
        }
    }
}

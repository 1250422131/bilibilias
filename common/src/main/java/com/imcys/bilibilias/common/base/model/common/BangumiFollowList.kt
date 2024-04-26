package com.imcys.bilibilias.common.base.model.common

/**
 * 用户追番数据类
 */
@kotlinx.serialization.Serializable
data class BangumiFollowList(
    val code: Int = 0,
    val message: String = "",
    val ttl: Int = 0,
    val data: DataBean = DataBean(),
) {
    @kotlinx.serialization.Serializable
    data class DataBean(
        val pn: Int = 0,
        val ps: Int = 0,
        val total: Int = 0,
        val list: List<ListBean> = emptyList(),
    ) {
        @kotlinx.serialization.Serializable
        data class ListBean(
            val season_id: Int = 0,
            val media_id: Int = 0,
            val season_type_name: String = "",
            val title: String = "",
            val cover: String = "",
            val total_count: Int = 0,
            val badge: String = "",
            val new_ep: NewEpBean = NewEpBean(),
            val season_title: String = "",
            val evaluate: String = "",
            val subtitle: String = "",
            val first_ep: Long = 0,
            val mode: Int = 0,
            val url: String = "",
            val badge_info: BadgeInfoBean = BadgeInfoBean(),
            val subtitle_14: String = "",
            val summary: String = "",
            val progress: String = "",
            val areas: List<AreasBean> = emptyList(),
        ) {
            @kotlinx.serialization.Serializable
            data class NewEpBean(
                val id: Int = 0,
                val index_show: String = "",
                val cover: String = "",
                val title: String = "",
                val long_title: String = "",
                val duration: Int = 0,
            )

            @kotlinx.serialization.Serializable
            data class BadgeInfoBean(
                val text: String = "",
                val bg_color: String = "",
            )

            @kotlinx.serialization.Serializable
            data class AreasBean(
                val id: Int = 0,
                val name: String = "",
            )
        }
    }
}

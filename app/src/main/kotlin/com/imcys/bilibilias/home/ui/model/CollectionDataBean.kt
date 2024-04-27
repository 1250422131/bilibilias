package com.imcys.bilibilias.home.ui.model

import kotlinx.serialization.Serializable

/**
 * 用户收藏夹详细内容
 */
@Serializable
data class CollectionDataBean(
    val code: Int = 0,
    val message: String = "",
    val ttl: Int = 0,
    val data: DataBean = DataBean()
) {
    @Serializable
    data class DataBean(val medias: List<MediasBean>? = emptyList()) {
        @Serializable
        data class MediasBean(
            val id: Long = 0,
            val type: Int = 0,
            val title: String = "",
            val cover: String = "",
            val duration: Int = 0,
            val attr: Int = 0,
            val fav_time: Long = 0,
            val bvid: String = ""
        )
    }
}

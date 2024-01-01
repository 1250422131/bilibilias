package com.imcys.bilibilias.home.ui.model

import kotlinx.serialization.Serializable

/**
 * 用户投稿类
 */
@Serializable
data class UserWorksBean(
    val code: Int = 0,
    val message: String = "",
    val ttl: Int = 0,
    val data: DataBean = DataBean(),
) {
    @Serializable
    data class DataBean(
        val list: ListBean = ListBean(),
        val page: PageBean = PageBean(),
    ) {
        @Serializable
        data class ListBean(
            val vlist: List<VlistBean> = emptyList()
        ) {
            @Serializable
            data class VlistBean(
                val aid: Long = 0,
                val description: String = "",
                val title: String = "",
                val author: String = "",
                val mid: Long = 0,
                val bvid: String = "",
                val pic: String,
                val play: Int,
                val video_review: Int
            )
        }

        @Serializable
        data class PageBean(
            val pn: Int = 0,
            val count: Int = 0
        )
    }
}

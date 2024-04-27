package com.imcys.bilibilias.home.ui.model

import kotlinx.serialization.Serializable

/**
 * 用户播放历史类
 */
@Serializable
data class PlayHistoryBean(
    val code: Int = 0,
    val message: String = "",
    val ttl: Int = 0,
    val data: DataBean = DataBean(),
) {
    @Serializable
    data class DataBean(
        val cursor: CursorBean = CursorBean(),
        val list: List<ListBean> = emptyList(),
    ) {
        @Serializable
        data class CursorBean(
            val max: Long = 0,
            val view_at: Long = 0,
        )

        @Serializable
        data class ListBean(
            val title: String = "",
            val long_title: String = "",
            val cover: String = "",
            val uri: String = "",
            val history: HistoryBean = HistoryBean(),
            val author_mid: Long = 0,
            val progress: Int = 0,
            val duration: Int = 0,
            val total: Int = 0,
        ) {
            @Serializable
            data class HistoryBean(
                val oid: Long = 0,
                val bvid: String = "",
                val cid: Long = 0,
                val part: String = "",
            )
        }
    }
}

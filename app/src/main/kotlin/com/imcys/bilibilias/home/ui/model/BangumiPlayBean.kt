package com.imcys.bilibilias.home.ui.model

import kotlinx.serialization.Serializable


/**
 * 番剧播放类
 */
@Serializable
data class BangumiPlayBean(
    val code: Int = 0,
    val message: String = "",
    val result: ResultBean = ResultBean(),
) {
    @Serializable
    data class ResultBean(
        val code: Int = 0,
        val type: String = "",
        val result: String = "",
        val from: String = "",
        val message: String = "",
        val status: Int = 0,
        val durl: List<DurlBean> = emptyList(),
        val accept_quality: List<Int> = emptyList(),
    ) {
        @Serializable
        data class DurlBean(
            val size: Long = 0,
            val url: String = "",
        )
    }
}

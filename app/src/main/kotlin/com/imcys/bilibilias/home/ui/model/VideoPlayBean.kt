package com.imcys.bilibilias.home.ui.model

import kotlinx.serialization.Serializable

/**
 * 视频下载信息数据类
 */
@Serializable
data class VideoPlayBean(
    val code: Int = 0,
    val message: String = "",
    val data: DataBean = DataBean()
) {
    @Serializable
    data class DataBean(
        val from: String = "",
        val result: String = "",
        val message: String = "",
        val durl: List<DurlBean> = emptyList(),
    )

    @Serializable
    data class DurlBean(
        val size: Long = 0,
        val url: String = "",
    )
}

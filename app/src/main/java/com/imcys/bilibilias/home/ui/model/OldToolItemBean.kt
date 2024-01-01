package com.imcys.bilibilias.home.ui.model

/**
 * 工具页类
 */
data class OldToolItemBean(
    val code: Int = 0,
    val data: List<DataBean> = emptyList(),
) {
    data class DataBean(
        val tool_code: Int = 0,
        val color: String = "",
        val title: String = "",
        val img_url: String = "",
    )
}

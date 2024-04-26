package com.imcys.bilibilias.home.ui.model

import kotlinx.serialization.Serializable

@Serializable
data class VideoPageListData(
    val code: Int = 0,
    val message: String = "",
    val data: List<DataBean> = emptyList(),
) {
    @Serializable
    data class DataBean(
        var selected: Int = 0,
        var checkState: Int = 0,
        val cid: Long = 0,
        val page: Int = 0,
        val part: String = "",
        val dimension: DimensionBean = DimensionBean(),
    ) {
        @Serializable
        data class DimensionBean(
            val width: Int = 0,
            val height: Int = 0,
            val rotate: Int = 0,
        )
    }
}

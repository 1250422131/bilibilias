package com.imcys.bilibilias.home.ui.model

import kotlinx.serialization.Serializable

/**
 * 首页推荐视频类
 */
@Serializable
data class HomeRCMDVideoBean(
    val code: Int = 0,
    val message: String = "",
    val ttl: Int = 0,
    val data: DataBean = DataBean(),
) {
    @Serializable
    data class DataBean(val item: List<ItemBean> = emptyList()) {
        @Serializable
        data class ItemBean(
            var likeState: Float = 0f,
            val id: Int = 0,
            val bvid: String = "",
            val cid: Long = 0,
            val uri: String = "",
            val pic: String = "",
            val title: String = "",
            val duration: Int = 0,
        )
    }
}

package com.imcys.bilibilias.home.ui.model

import kotlinx.serialization.Serializable

/**
 * 用户创建收藏夹数据
 */
@Serializable
data class UserCreateCollectionBean(
    val code: Int = 0,
    val message: String = "",
    val ttl: Int = 0,
    val data: DataBean = DataBean(),
) {
    @Serializable
    data class DataBean(val list: List<ListBean> = emptyList()) {
        @Serializable
        data class ListBean(
            val id: Long = 0,
            val mid: Long = 0,
            val attr: Int = 0,
            val title: String = "",
            val media_count: Int = 0,
            var selected: Int = 0,
        )
    }
}

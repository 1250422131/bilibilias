package com.imcys.bilibilias.home.ui.model

import kotlinx.serialization.Serializable

/**
 * B站用户卡片信息类
 */
@Serializable
data class UserCardBean(
    val code: Int = 0,
    val message: String = "",
    val ttl: Int = 0,
    val data: DataBean = DataBean()
) {
    @Serializable
    data class DataBean(
        val card: CardBean = CardBean(),
        val archive_count: Int = 0
    ) {
        @Serializable
        data class CardBean(
            val mid: String = "",
            val name: String = "",
            val face: String = "",
            val description: String = "",
            val fans: Int = 0,
            val friend: Int = 0,
            val sign:String = ""
        )
    }
}

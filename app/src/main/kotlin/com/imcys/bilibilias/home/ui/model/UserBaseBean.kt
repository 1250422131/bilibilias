package com.imcys.bilibilias.home.ui.model

import kotlinx.serialization.Serializable

/**
 * 用户基本信息类
 */
@Serializable
data class UserBaseBean(
    val code: Int = 0,
    val message: String = "",
    val ttl: Int = 0,
    val data: DataBean = DataBean(),
) {
    @Serializable
    data class DataBean(
        val mid: Long = 0,
        val name: String = "",
        val face: String = "",
        val sign: String = "",
        val level: Int = 0,
        val vip: VipBean = VipBean(),
    ) {
        @Serializable
        data class VipBean(
            val type: Int = 0,
            val status: Int = 0,
            val nickname_color: String = "",
        )
    }
}

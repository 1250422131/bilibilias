package com.imcys.bilibilias.base.model.user

import kotlinx.serialization.Serializable

/**
 * 用户常用数据
 */
@Serializable
data class UserInfoBean(
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
    )
}

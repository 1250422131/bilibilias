package com.imcys.bilibilias.common.base.model.user

import kotlinx.serialization.Serializable

/**
 * 我的个人信息类
 */
@Serializable
data class MyUserData(
    val code: Int = 0,
    val message: String = "",
    val ttl: Int = 0,
    val data: DataBean = DataBean(),
) {
    @Serializable
    data class DataBean(
        val mid: Long = 0,
        val uname: String = "",
        val sign: String = "",
        val sex: String = "",
        val rank: String = "",
    )
}

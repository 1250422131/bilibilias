package com.imcys.bilibilias.core.model

import kotlinx.serialization.Serializable

/**
 * 登陆状态数据
 */
@Serializable
data class LoginStateBean(
    val code: Int = 0,
    val message: String = "",
    val ttl: Int = 0,
    val data: DataBean = DataBean(),
) {
    @Serializable
    data class DataBean(
        val url: String = "",
        val refresh_token: String = "",
        val code: Int = 0,
        val message: String = "",
    )
}
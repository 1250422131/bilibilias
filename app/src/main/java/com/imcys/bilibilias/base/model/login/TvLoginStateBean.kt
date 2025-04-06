package com.imcys.bilibilias.base.model.login

import kotlinx.serialization.Serializable

/**
 * 登陆状态数据
 */
@Serializable
data class TvLoginStateBean(
    val code: Int = 0,
    val message: String = "",
    val ttl: Int = 0,
    val data: DataBean? = DataBean(),
) {
    @Serializable
    data class DataBean(
        val mid: Long = 0,
        val access_token: String = "",
        val refresh_token: String = "",
        val expires_in: Long = 0,
    )
}


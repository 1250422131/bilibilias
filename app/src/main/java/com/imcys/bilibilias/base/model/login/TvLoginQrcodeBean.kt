package com.imcys.bilibilias.base.model.login

import kotlinx.serialization.Serializable


/**
 * 登陆二维码数据
 */
@Serializable
data class TvLoginQrcodeBean(
    val code: Int = 0,
    val message: String = "",
    val data: DataBean = DataBean(),
) {
    @Serializable
    data class DataBean(
        var url: String = "",
        var auth_code: String = "",
    )
}

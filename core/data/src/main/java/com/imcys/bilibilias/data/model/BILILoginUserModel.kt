package com.imcys.bilibilias.data.model

import kotlinx.serialization.Serializable

/**
 * 登录用户信息
 */
@Serializable
data class BILILoginUserModel(
    val face: String?,
    val level: Int?,
    val mid: Long?,
    val name: String?,
    val vipState:Int?
)

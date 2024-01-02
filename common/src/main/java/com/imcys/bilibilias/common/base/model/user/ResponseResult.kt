package com.imcys.bilibilias.common.base.model.user

import kotlinx.serialization.Serializable


/**
 * 响应类
 * @property code Int
 * @property msg String
 * @constructor
 */
@Serializable
open class ResponseResult<T>(
    open val code: Int, // 0
    open val msg: String, // 登录成功
    open val data: T
)
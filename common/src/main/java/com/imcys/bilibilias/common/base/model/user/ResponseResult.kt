package com.imcys.bilibilias.common.base.model.user

import com.google.gson.annotations.SerializedName
import java.io.Serializable

/**
 * 响应类
 * @property code Int
 * @property msg String
 * @constructor
 */
open class ResponseResult(
    @SerializedName("code")
    open val code: Int, // 0
    @SerializedName("msg")
    open val msg: String, // 登录成功
) : Serializable
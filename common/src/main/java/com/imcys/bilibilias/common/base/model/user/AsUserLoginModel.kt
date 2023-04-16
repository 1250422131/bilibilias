package com.imcys.bilibilias.common.base.model.user

import com.google.gson.annotations.SerializedName
import java.io.Serializable

/**
 * bilibilias云端账户登录结果
 * @property code Int
 * @property msg String
 * @constructor
 */

data class AsUserLoginModel(
    @SerializedName("code")
     val code: Int, // 0
    @SerializedName("msg")
    val msg: String, // 登录成功
) : Serializable
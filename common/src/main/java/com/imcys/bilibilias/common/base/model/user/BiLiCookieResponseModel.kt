package com.imcys.bilibilias.common.base.model.user

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class BiLiCookieResponseModel(
    @SerializedName("code")
    val code: Int, // 0
    @SerializedName("msg")
    val msg: String, // 登录成功
) : Serializable

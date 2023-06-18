package com.imcys.bilibilias.common.base.model.user

import com.google.gson.annotations.SerializedName
import com.imcys.bilibilias.common.base.model.common.IPostBody
import java.io.Serializable


data class UserBiliBiliCookieModel(
    @SerializedName("code")
    val code: Int, // 0
    @SerializedName("data")
    val `data`: List<Data>,
    @SerializedName("msg")
    val msg: String, // 获取成功啦
) : Serializable {
    data class Data(
        @SerializedName("cookie")
        val cookie: String, // adda
        @SerializedName("face")
        val face: String, // https://i1.hdslb.com/bfs/garb/item/c90b1b7d971d0a6f92f10ac3b9ae3c7e80fe2dc8.png
        @SerializedName("level")
        val level: Int, // 6
        @SerializedName("name")
        val name: String, // 萌新杰少
        @SerializedName("type")
        val type: Int, // 1
    ) : Serializable, IPostBody
}
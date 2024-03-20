package com.imcys.bilibilias.core.model

import kotlinx.serialization.Serializable

@Serializable
data class UserBiliBiliCookieModel(
    val code: Int, // 0
    val `data`: List<Data>,
    val msg: String, // 获取成功啦
) {
    @Serializable
    data class Data(
        val cookie: String,
        val face: String,
        val level: Int,
        val name: String,
        val type: Int,
    ) : IPostBody
}
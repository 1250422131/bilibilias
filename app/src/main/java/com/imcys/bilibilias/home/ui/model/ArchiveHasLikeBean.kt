@file:JvmName("ArchiveHasLikeBean")

package com.imcys.bilibilias.home.ui.model

import kotlinx.serialization.Serializable

/**
 * @author:imcys
 * @create: 2023-01-03 16:03
 * @Description: 检验是否被点赞
 */
@Serializable
data class ArchiveHasLikeBean(
    val code: Int,
    val message: String,
    val ttl: Int,
    var data: Int
):java.io.Serializable

@file:JvmName("ArchiveHasLikeBean")

package com.imcys.bilibilias.common.base.model.video

import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

/**
 * @author:imcys
 * @create: 2023-01-03 16:03
 * @Description: 检验是否被点赞
 * 0：未点赞
 * 1：已点赞
 */
@Serializable
data class VideoHasLike(val data: Int = 0) {
    @Transient
    val like: Boolean = data == 1
}

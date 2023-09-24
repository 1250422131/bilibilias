@file:JvmName("ArchiveHasLikeBean")

package com.imcys.bilibilias.common.base.model.video

import kotlinx.serialization.Serializable

/**
 * @author:imcys
 * @create: 2023-01-03 16:03
 * @Description: 检验是否被点赞
 */
/**
 * code : 0
 * message : 0
 * ttl : 1
 * data : 1
 */
@Serializable
data class VideoHasLike(
    var data: Int = 0
)

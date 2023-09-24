package com.imcys.bilibilias.common.base.model.video

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * @author:imcys
 * @create: 2023-01-03 16:23
 * @Description: 收藏检验
 *
 * code : 0
 * message : 0
 * ttl : 1
 * data : {"count":1,"favoured":true}
 */

@Serializable
data class VideoCollection(
    @SerialName("favoured")
    var isFavoured: Boolean = false
)
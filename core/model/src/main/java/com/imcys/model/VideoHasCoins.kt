package com.imcys.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

/**
 * @author:imcys
 * @create: 2023-01-03 16:18
 * @Description: 投币情况嘞
 */

/**
 * multiply : 2
 */
@Serializable
data class VideoHasCoins(val multiply: Int = 0) {
    @Transient
    val coins: Boolean = multiply > 0
}

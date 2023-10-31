package com.imcys.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * code : 0
 * message : 0
 * ttl : 1
 * data : {"count":1,"favoured":true}
 */

@Serializable
data class VideoCollection(
    @SerialName("favoured")
    val isFavoured: Boolean = false
)

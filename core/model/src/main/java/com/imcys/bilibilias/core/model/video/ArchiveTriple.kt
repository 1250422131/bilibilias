package com.imcys.bilibilias.core.model.video

import com.imcys.bilibilias.core.model.Response
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

typealias ArchiveLike = Response

@Serializable
data class ArchiveCoins(
    @SerialName("multiply")
    val multiply: Int = 0
) {
    val hasCoins = multiply != 0
}

@Serializable
data class ArchiveFavoured(
    @SerialName("favoured")
    val favoured: Boolean = false
)

data class ViewTriple(val hasLike: Boolean, val hasCoins: Boolean, val hasFavoured: Boolean)
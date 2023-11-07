package com.imcys.model.video

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Author(
    @SerialName("mid") val mid: Long = 0,
    @SerialName("name") val name: String = "",
    @SerialName("sex") val sex: String = "",
    @SerialName("face") val face: String = "",
    @SerialName("sign") val sign: String = "",
    @SerialName("rank") val rank: Int = 0,
    @SerialName("birthday") val birthday: Int = 0,
    @SerialName("is_fake_account") val isFakeAccount: Int = 0,
    @SerialName("is_deleted") val isDeleted: Int = 0,
)
    
package com.imcys.bilibilias.core.model.user
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
@Serializable
data class LevelInfo(
    @SerialName("current_exp")
    val currentExp: Int = 0,
    @SerialName("current_level")
    val currentLevel: Int = 0,
    @SerialName("current_min")
    val currentMin: Int = 0,
)

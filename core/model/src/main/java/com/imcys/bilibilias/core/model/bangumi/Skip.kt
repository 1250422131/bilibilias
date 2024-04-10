package com.imcys.bilibilias.core.model.bangumi

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Skip(
    @SerialName("ed")
    val ed: Ed = Ed(),
    @SerialName("op")
    val op: Op = Op()
) {
    @Serializable
    data class Ed(
        @SerialName("end")
        val end: Int = 0,
        @SerialName("start")
        val start: Int = 0
    )

    @Serializable
    data class Op(
        @SerialName("end")
        val end: Int = 0,
        @SerialName("start")
        val start: Int = 0
    )
}

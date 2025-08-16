package com.imcys.bilibilias.core.datasource.model


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserProfile(
    @SerialName("birthday")
    val birthday: String = "",
    @SerialName("mid")
    val mid: Long = 0,
    @SerialName("nick_free")
    val nickFree: Boolean = false,
    @SerialName("rank")
    val rank: String = "",
    @SerialName("sex")
    val sex: String = "",
    @SerialName("sign")
    val sign: String = "",
    @SerialName("uname")
    val uname: String = "",
    @SerialName("userid")
    val userid: String = ""
)
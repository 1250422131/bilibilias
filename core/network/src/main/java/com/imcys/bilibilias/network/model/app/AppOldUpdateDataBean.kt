package com.imcys.bilibilias.network.model.app

import kotlinx.serialization.Serializable

@Serializable
data class AppOldUpdateDataBean (
    val code: Int = 0,
    val version: String = "",
    val gxnotice: String = "",
    val notice: String = "",
    val url: String = "",
    val apkmD5: String = "",
    val apkToKen: String = "",
    val apkToKenCR: String = "",
    val id: String = "",
    val gray:Boolean = false,
)
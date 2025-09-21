package com.imcys.bilibilias.network.model.app

import kotlinx.serialization.Serializable

@Serializable
data class AppUpdateConfigInfo(
    val id: Long,
    val version: String,
    val feat: String,
    val fix: String,
    val remark: String,
    val forcedUpdate: Boolean,
    val publishDateTime: String
)
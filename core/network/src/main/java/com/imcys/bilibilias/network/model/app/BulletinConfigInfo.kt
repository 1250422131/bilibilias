package com.imcys.bilibilias.network.model.app

import kotlinx.serialization.Serializable


@Serializable
data class BulletinConfigInfo(
    val id: Int,
    val content: String,
    val publishDateTime: String,
)

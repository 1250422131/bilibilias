package com.imcys.bilibilias.network.model.app

import kotlinx.serialization.Serializable

@Serializable
data class BannerConfigInfo(
    val id: Int,
    val title: String,
    val url: String,
    val sort: Int,
)

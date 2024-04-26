package com.imcys.bilibilias.home.ui.model

import kotlinx.serialization.Serializable

@Serializable
data class UpStatBeam(
    val code: Int = 0,
    val message: String = "",
    val ttl: Int = 0,
    val data: DataBean = DataBean(),
) {
    @Serializable
    data class DataBean(
        val archive: ArchiveBean = ArchiveBean(),
        val likes: Int = 0,
    ) {
        @Serializable
        data class ArchiveBean(val view: Int = 0)
    }
}

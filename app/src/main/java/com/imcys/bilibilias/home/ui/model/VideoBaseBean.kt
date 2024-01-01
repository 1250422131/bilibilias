package com.imcys.bilibilias.home.ui.model

import kotlinx.serialization.Serializable

@Serializable
data class VideoBaseBean(
    val code: Int = 0,
    val message: String = "",
    val data: DataBean = DataBean(),
) {
    @Serializable
    data class DataBean(
        val bvid: String = "",
        val aid: Long = 0,
        val tname: String = "",
        val copyright: Int = 0,
        val pic: String = "",
        val title: String = "",
        val desc: String = "",
        val duration: Int = 0,
        val owner: OwnerBean = OwnerBean(),
        val stat: StatBean = StatBean(),
        val cid: Long = 0,
        val redirect_url: String = "",
    )

    @Serializable
    data class OwnerBean(
        val mid: Long = 0,
        val name: String = "",
        val face: String = "",
    )

    @Serializable
    data class StatBean(
        val view: Int = 0,
        val danmaku: Int = 0,
        val favorite: Int = 0,
        val coin: Int = 0,
        val share: Int = 0,
        val like: Int = 0
    )
}

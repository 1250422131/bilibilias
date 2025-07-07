package com.imcys.bilibilias.network.model.user


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * 视频和文章获得的阅读次数
 */
@Serializable
data class BILIUserSpaceUpStat(
    @SerialName("archive")
    val archive: Archive,
    @SerialName("article")
    val article: Article,
    @SerialName("likes")
    val likes: Long = 0
) {
    @Serializable
    data class Archive(
        @SerialName("enable_vt")
        val enableVt: Long = 0,
        @SerialName("view")
        val view: Long = 0,
        @SerialName("vt")
        val vt: Long = 0,
    )

    @Serializable
    data class Article(
        @SerialName("view")
        val view: Long = 0,
    )
}
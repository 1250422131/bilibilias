package com.imcys.bilibilias.logic.search

data class Episode(
    val cid: Long,
    val index: Int,
    val part: String,
)

data class EpisodeQuality(
    val description: String,
    val quality: Int,
)
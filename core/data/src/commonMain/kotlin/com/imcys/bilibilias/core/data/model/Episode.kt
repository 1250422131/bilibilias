package com.imcys.bilibilias.core.data.model

data class Episode(
    val aid: Long,
    val bvid: String,
    val desc: String,
    val cover: String,
    val title: String,
    val owner: Owner,
    val series: List<EpisodeItem>,
    val qualities: List<Quality> = emptyList()
)
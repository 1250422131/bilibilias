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

data class EpisodeInfo(
    val bvid: String,
    val desc: String,
    val cover: String,
    val title: String,
    val owner: Owner,
    val parts: List<EpisodePartInfo>,
    val video: List<EpisodeSource>,
    val audio: List<EpisodeSource>,
)

data class EpisodeSource(
    val id: String,
    val codecs: Int,
    val baseUrl: String,
    val backupUrl: List<String>
)

data class EpisodePartInfo(
    val cid: Long,
    val title: String,
    val index: Int
)

data class Owner(
    val id: Long,
    val avatarUrl: String,
    val name: String
)
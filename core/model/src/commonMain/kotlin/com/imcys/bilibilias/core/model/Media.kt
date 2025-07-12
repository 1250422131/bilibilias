package com.imcys.bilibilias.core.model

data class CacheData(
    val bvid: String,
    val cid: Long,
    val title: String,
    val cover: String,
    val pub: Long,
    val videos: MediaSources,
    val audios: MediaSources
)

data class MediaSources(
    val url: String,
    val id: Int,
    val codecs: Int = 0,
    val height: Int = 0,
    val width: Int = 0
)
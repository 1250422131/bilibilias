package com.imcys.bilibilias.core.data.model

data class EpisodeCacheRequest(
    val episodeSubId: Long,
    val videoResolution: Int,
    val audioResolution: Int,
)
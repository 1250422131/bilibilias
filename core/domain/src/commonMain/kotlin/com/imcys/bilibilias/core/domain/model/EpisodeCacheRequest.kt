package com.imcys.bilibilias.core.domain.model

data class EpisodeCacheRequest(
    val cacheState: EpisodeCacheState,
    val videoQuality: Int,
    val audioQuality: Int,
)
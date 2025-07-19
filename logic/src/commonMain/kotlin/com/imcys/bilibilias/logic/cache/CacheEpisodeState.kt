package com.imcys.bilibilias.logic.cache

import com.imcys.bilibilias.core.media.cache.EpisodeMetadata
import com.imcys.bilibilias.core.media.cache.MediaCacheMetadata

data class CacheEpisodeState(
    val episodeMetadata: EpisodeMetadata,
    val mediaCacheMetadata: MediaCacheMetadata,
    val fileStats: FileStats,
    val canPlay: Boolean,
)

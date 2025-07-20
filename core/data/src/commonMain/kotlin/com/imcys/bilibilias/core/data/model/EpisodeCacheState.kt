package com.imcys.bilibilias.core.data.model

data class EpisodeCacheListState(
    val episodeInfo: EpisodeInfo2,
    val episodes: List<EpisodeCacheState>,
    val videoStreams: List<MediaStream>,
    val audioStreams: List<MediaStream>,
)

data class EpisodeInfo2(
    val episodeId: String,
    val episodeSubId: Long,
    val title: String,
    val desc: String,
    val cover: String,
)

data class EpisodeCacheState(
    val episodeSubId: Long,
    val title: String,
    val cacheStatus: EpisodeCacheStatus
)

data class MediaStream(
    val id: Int,
    val description: String,
)

sealed interface EpisodeCacheStatus {

    /**
     * At least one cache is fully downloaded.
     */
    data object Cached : EpisodeCacheStatus

    /**
     * No cache is fully downloaded, but at least one cache is downloading.
     */
    data object Caching : EpisodeCacheStatus

    data object NotCached : EpisodeCacheStatus
}

package com.imcys.bilibilias.core.domain.model

import com.imcys.bilibilias.core.coroutines.MonoTasker

data class EpisodeCacheListState(
    val episodeInfo: EpisodeInfo2,
    val episodes: List<EpisodeCacheState>,
    val videoStreams: List<MediaStream>,
    val audioStreams: List<MediaStream>,
) {
    companion object {
        val Placeholder = EpisodeCacheListState(
            episodeInfo = EpisodeInfo2("", "", ""),
            episodes = emptyList(),
            videoStreams = emptyList(),
            audioStreams = emptyList()
        )
    }
}

data class EpisodeInfo2(
    val title: String,
    val desc: String,
    val cover: String,
)

data class EpisodeCacheState(
    val episodeId: String,
    val episodeSubId: Long,
    val title: String,
    val index: Int,
    val cacheStatus: EpisodeCacheStatus,
    val actionTasker: MonoTasker
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

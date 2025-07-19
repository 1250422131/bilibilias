package com.imcys.bilibilias.logic.cache

import com.imcys.bilibilias.core.format.DataSize.Companion.bytes
import com.imcys.bilibilias.core.http.downloader.model.DownloadId
import com.imcys.bilibilias.core.http.downloader.model.DownloadProgress
import com.imcys.bilibilias.core.http.downloader.model.DownloadStatus
import com.imcys.bilibilias.core.media.cache.EpisodeMetadata
import com.imcys.bilibilias.core.media.cache.MediaCacheMetadata
import com.imcys.bilibilias.logic.utils.DataStoreProvider
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf

object MediaCacheManager {
    private val httpDownloader = DataStoreProvider.httpDownloader
    private val mediaCacheStorage = DataStoreProvider.mediaCacheStorage

    fun observeCachedEpisodeStates(): Flow<List<CacheEpisodeState>> {
        return mediaCacheStorage.listFlow.flatMapLatest { mediaCacheSaves ->
            if (mediaCacheSaves.isEmpty()) {
                flowOf(emptyList())
            } else {
                val episodeStateFlows = mediaCacheSaves.map { (episodeMetadata, cacheMetadata) ->
                    createEpisodeStateFlow(episodeMetadata, cacheMetadata)
                }
                combine(episodeStateFlows) { statesArray ->
                    statesArray.toList()
                }
            }
        }
    }

    private fun createEpisodeStateFlow(
        episodeMetadata: EpisodeMetadata,
        mediaCacheMetadata: MediaCacheMetadata
    ): Flow<CacheEpisodeState> {
        val downloadPartProgressFlows = mediaCacheMetadata.metadata.map { metaItem ->
            httpDownloader.getProgressFlow(DownloadId(metaItem.downloadId))
        }

        return if (downloadPartProgressFlows.isEmpty()) {
            flowOf(createDefaultCacheEpisodeState(episodeMetadata, mediaCacheMetadata))
        } else {
            combine(downloadPartProgressFlows) { progressArray ->
                calculateCacheEpisodeState(episodeMetadata, mediaCacheMetadata, progressArray)
            }
        }
    }

    private fun createDefaultCacheEpisodeState(
        episodeMetadata: EpisodeMetadata,
        mediaCacheMetadata: MediaCacheMetadata
    ): CacheEpisodeState {
        return CacheEpisodeState(
            episodeMetadata = episodeMetadata,
            mediaCacheMetadata = mediaCacheMetadata,
            fileStats = FileStats.Unspecified,
            canPlay = false
        )
    }

    private fun calculateCacheEpisodeState(
        episodeMetadata: EpisodeMetadata,
        mediaCacheMetadata: MediaCacheMetadata,
        progressArray: Array<DownloadProgress>
    ): CacheEpisodeState {
        val allCompleted = progressArray.all { it.status == DownloadStatus.COMPLETED }
        val totalBytes = progressArray.sumOf { it.totalBytes }
        val downloadedBytes = progressArray.sumOf { it.downloadedBytes }

        val downloadProgress = if (totalBytes > 0L) {
            (downloadedBytes.toFloat() / totalBytes.toFloat()).toProgress()
        } else {
            Progress.Unspecified
        }

        val fileStats = FileStats(
            totalSize = totalBytes.bytes,
            downloadedBytes = downloadedBytes.bytes,
            downloadProgress = if (allCompleted) 1f.toProgress() else downloadProgress
        )

        return CacheEpisodeState(
            episodeMetadata = episodeMetadata,
            mediaCacheMetadata = mediaCacheMetadata,
            fileStats = fileStats,
            canPlay = allCompleted
        )
    }
}

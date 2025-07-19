package com.imcys.bilibilias.logic.cache

import com.imcys.bilibilias.core.format.DataSize.Companion.bytes
import com.imcys.bilibilias.core.http.downloader.model.DownloadId
import com.imcys.bilibilias.core.http.downloader.model.DownloadStatus
import com.imcys.bilibilias.logic.utils.DataStoreProvider
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.all
import kotlinx.coroutines.flow.count
import kotlinx.coroutines.flow.map

object MediaCacheManager {
    private val httpDownloader = DataStoreProvider.httpDownloader
    private val mediaCacheStorage = DataStoreProvider.mediaCacheStorage

    fun cachedEpisodesFlow(): Flow<List<CacheEpisodeState>> {
        return mediaCacheStorage.listFlow.map { saves ->
            saves.map { (origin, mediaCacheMetadata) ->
                val canPlay = mediaCacheMetadata.metadata.map {
                    httpDownloader.getProgressFlow(DownloadId(it.downloadId))
                }.all { downloadProgress ->
                    downloadProgress.all {
                        it.status == DownloadStatus.COMPLETED
                    }
                }

                val state = mediaCacheMetadata.metadata.map {
                    httpDownloader.getProgressFlow(DownloadId(it.downloadId))
                }.map { downloadProgress ->
                    val totalSize = downloadProgress.map { it.totalBytes }.count()
                    val downloadedBytes = downloadProgress.map { it.downloadedBytes }.count()
                    totalSize to downloadedBytes
                }.fold(FileStats.Unspecified) { _, fileStats ->
                    FileStats(
                        totalSize = fileStats.first.bytes,
                        downloadedBytes = fileStats.second.bytes,
                        downloadProgress = if (canPlay) {
                            1f.toProgress()
                        } else {
                            Progress.Unspecified
                        }
                    )
                }
                CacheEpisodeState(
                    episodeMetadata = origin,
                    mediaCacheMetadata = mediaCacheMetadata,
                    fileStats = state,
                    canPlay = canPlay
                )
            }
        }
    }
}
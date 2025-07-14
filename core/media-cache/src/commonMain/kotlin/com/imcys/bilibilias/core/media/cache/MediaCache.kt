package com.imcys.bilibilias.core.media.cache

import com.imcys.bilibilias.core.format.DataSize
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

data class MediaCache(
    val origin: EpisodeMetadata,
    val metadata: MediaCacheMetadata
) {
    val state: StateFlow<MediaCacheState> = MutableStateFlow(MediaCacheState.IN_PROGRESS)

    val canPlay: StateFlow<Boolean> = MutableStateFlow(false)

    val fileStats: StateFlow<FileStats> = MutableStateFlow(FileStats.Unspecified)
//    suspend inline fun MediaCache.isFinished(): Boolean = downloadProgress.isFinished
    /**
     * @see TorrentFileEntry.Stats
     * @see TorrentMediaCacheEngine.TorrentMediaCache.fileStats
     */
    data class FileStats(
        val totalSize: DataSize,
        /**
         * 已经下载成功的字节数.
         *
         * @return `0L`..[TorrentFileEntry.length]
         * @see TorrentFileEntry.Stats.downloadedBytes
         */
        val downloadedBytes: DataSize,
//        /**
//         * 已完成比例.
//         *
//         * @return `0f`..`1f`, 在未开始下载时, 该值为 [Progress.Unspecified].
//         */
//        val downloadProgress: Progress = if (totalSize.isUnspecified || downloadedBytes.isUnspecified) {
//            Progress.Unspecified
//        } else {
//            if (totalSize.inBytes == 0L) {
//                0f.toProgress()
//            } else {
//                (downloadedBytes.inBytes.toFloat() / totalSize.inBytes).toProgress()
//            }
//        },

        // 没有上传信息
        // hint: 要获取下载速度: downloadedBytes.sampleWithInitial(1000).averageRate()
    ) {
        /**
         * 下载是否已经完成.
         *
         * 在刚创建时, [MediaCache] 可能需要时间扫描已经下载的文件状态.
         * 在扫描完成前, 即使文件已经下载成功, 该值也为 `false`.
         */
//        val isDownloadFinished: Boolean get() = downloadProgress.isFinished

        companion object {
            val Unspecified =
                FileStats(
                    DataSize.Unspecified,
                    DataSize.Unspecified,
//                    Progress.Unspecified,
                )
        }
    }
}

enum class MediaCacheState {
    IN_PROGRESS,
    PAUSED,
}

package com.imcys.bilibilias.logic.cache

import com.imcys.bilibilias.core.format.DataSize
import com.imcys.bilibilias.core.media.cache.EpisodeMetadata

data class EpisodeCacheState(
    val episodeMetadata: EpisodeMetadata,
//    val canPlay: Boolean,
//    val fileStats: FileStats,
)

data class FileStats(
    val totalSize: DataSize,
    /**
     * 已经下载成功的字节数.
     *
     */
    val downloadedBytes: DataSize,
    /**
     * 已完成比例.
     *
     * @return `0f`..`1f`, 在未开始下载时, 该值为 [Progress.Unspecified].
     */
    val downloadProgress: Progress = if (totalSize.isUnspecified || downloadedBytes.isUnspecified) {
        Progress.Unspecified
    } else {
        if (totalSize.inWholeBytes == 0L) {
            0f.toProgress()
        } else {
            (downloadedBytes.inWholeBytes.toFloat() / totalSize.inWholeBytes).toProgress()
        }
    },
) {
    /**
     * 下载是否已经完成.
     *
     * 在刚创建时, [MediaCache] 可能需要时间扫描已经下载的文件状态.
     * 在扫描完成前, 即使文件已经下载成功, 该值也为 `false`.
     */
    val isDownloadFinished: Boolean get() = downloadProgress.isFinished

    companion object {
        val Unspecified =
            FileStats(
                DataSize.Unspecified,
                DataSize.Unspecified,
                Progress.Unspecified,
            )
    }
}

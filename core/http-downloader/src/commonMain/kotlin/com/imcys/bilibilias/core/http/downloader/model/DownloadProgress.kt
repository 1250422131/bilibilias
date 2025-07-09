package com.imcys.bilibilias.core.http.downloader.model

import kotlinx.serialization.Serializable

@Serializable
data class DownloadProgress(
    val downloadId: DownloadId,
    val url: String,
    val totalSegments: Int,
    val downloadedSegments: Int,
    val downloadedBytes: Long,
    val totalBytes: Long, // -1 for unknown
    val status: DownloadStatus,
    val error: DownloadError? = null,
)

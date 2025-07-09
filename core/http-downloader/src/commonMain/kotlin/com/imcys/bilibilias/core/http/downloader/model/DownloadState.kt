package com.imcys.bilibilias.core.http.downloader.model

import com.imcys.bilibilias.core.http.downloader.MediaType
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable // saved in data store
data class DownloadState(
    val downloadId: DownloadId,
    val url: String,
    @SerialName("outputPath")
    val relativeOutputPath: String,
    val segments: List<SegmentInfo>,
    val totalSegments: Int,
    val downloadedBytes: Long,
    val timestamp: Long,
    val status: DownloadStatus,
    val error: DownloadError? = null,
    @SerialName("segmentCacheDir")
    val relativeSegmentCacheDir: String,
    val mediaType: MediaType,
)
package com.imcys.bilibilias.core.http.downloader.model

import kotlinx.serialization.Serializable

@Serializable
enum class DownloadStatus {
    INITIALIZING,
    DOWNLOADING,
    MERGING,
    PAUSED,
    COMPLETED,
    FAILED,
    CANCELED
}
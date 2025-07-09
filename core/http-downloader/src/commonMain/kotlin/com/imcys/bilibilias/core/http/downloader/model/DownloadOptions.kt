package com.imcys.bilibilias.core.http.downloader.model

import kotlinx.serialization.Serializable

@Serializable
data class DownloadOptions(
    val maxConcurrentSegments: Int = 3,
    val segmentRetryCount: Int = 3,
    val connectTimeoutMs: Long = 30_000,
    val readTimeoutMs: Long = 30_000,
    val autoSaveIntervalMs: Long = 5_000,
    val headers: Map<String, String> = emptyMap(),
    val maxRetriesPerSegment: Int = 100,
    val baseRetryDelayMillis: Long = 1000L,
)
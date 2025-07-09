package com.imcys.bilibilias.core.http.downloader.model

import kotlinx.serialization.Serializable

@Serializable
data class DownloadError(
    val code: DownloadErrorCode,
    val params: Map<String, String> = emptyMap(),
    val technicalMessage: String? = null,
)
package com.imcys.bilibilias.core.http.downloader.model

import kotlinx.serialization.Serializable

@Serializable
enum class DownloadErrorCode {
    NO_MEDIA_LIST,
    UNEXPECTED_ERROR,
}
package com.imcys.bilibilias.core.network.download

sealed interface DownloadResult {
    data object Success : DownloadResult
    data class Progress(val percent: Float) : DownloadResult
    data class Error(val message: String) : DownloadResult
}

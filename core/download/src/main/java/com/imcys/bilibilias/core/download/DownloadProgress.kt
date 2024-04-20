package com.imcys.bilibilias.core.download

import com.liulishuo.okdownload.DownloadTask

data class DownloadProgress(
    val task: DownloadTask,
    val currentOffset: Long,
    val totalOffset: Long
) {
    companion object {
        private const val UNKNOWN_TOTAL_OFFSET = -1L
        const val UNKNOWN_PROGRESS = 0f
    }

    fun totalUnknown(): Boolean = totalOffset == UNKNOWN_TOTAL_OFFSET

    fun progress(): Float = when (totalOffset) {
        UNKNOWN_TOTAL_OFFSET -> UNKNOWN_PROGRESS
        0L -> if (currentOffset == 0L) 1f else UNKNOWN_PROGRESS
        else -> currentOffset * 1.0f / totalOffset
    }
}

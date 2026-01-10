package com.imcys.bilibilias.ui.download

import com.imcys.bilibilias.database.entity.download.DownloadSegment

/**
 * 下载页面 UI 事件
 */
sealed interface DownloadUiEvent {
    data class ShowToast(val message: String) : DownloadUiEvent
    data class OpenFile(val segment: DownloadSegment) : DownloadUiEvent
}

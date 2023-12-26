package com.imcys.player.state

import com.imcys.model.video.Owner
import com.imcys.model.video.PageData
import com.imcys.model.video.ToolBarReport

sealed interface PlayInfoUiState {
    data object Loading : PlayInfoUiState
    data object LoadFailed : PlayInfoUiState
    data class Success(
        val aid: Long,
        val bvid: String,
        val cid: Long,
        val title: String,
        val pic: String,
        val desc: String,
        val pageData: List<PageData>,
        val owner: Owner,
        val toolBarReport: ToolBarReport = ToolBarReport()
    ) : PlayInfoUiState
}

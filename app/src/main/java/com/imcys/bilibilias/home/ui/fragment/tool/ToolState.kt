package com.imcys.bilibilias.home.ui.fragment.tool

import com.imcys.bilibilias.core.model.VideoBaseBean

data class ToolState(
    val items: List<ToolItem> = emptyList(),
    val videoInfo: VideoBaseBean = VideoBaseBean()
)

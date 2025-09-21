package com.imcys.bilibilias.data.model.user

import com.imcys.bilibilias.network.model.user.BILIUserHistoryPlayInfo.ItemData.History

data class BILIUserHistoryPlayModel(
    val longTitle: String,
    val progress: Long,
    val duration: Long,
    val showTitle: String,
    val tagName: String,
    val title: String,
    val cover: String,
    val history: History,
    val max: Long = 0,
    val viewAt: Long = 0,
    val authorName: String = "",
    val authorMid: Long = 0,
)
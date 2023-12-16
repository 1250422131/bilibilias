package com.imcys.bilias.feature.merge.danmaku.model

import com.imcys.bilias.feature.merge.ass.model.V4Style

data class Danmaku(
    val time: Long,
    val type: DanmakuType,
    val fontSize: V4Style,
    val text: String,
    val color: Long,
)

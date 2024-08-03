package com.imcys.bilias.feature.merge.danmaku.model

import com.imcys.bilias.feature.merge.ass.model.V4Style

enum class FontSize(val size: Int) {
    Small(18),
    Medium(25),
    Large(36),
    ;

    companion object {
        fun valueOf(size: Int): V4Style = when (size) {
            18 -> com.imcys.bilias.feature.merge.ass.model.Small
            25 -> com.imcys.bilias.feature.merge.ass.model.Medium
            36 ->com.imcys.bilias.feature.merge.ass.model. Large
            else -> com.imcys.bilias.feature.merge.ass.model.Medium
        }
    }
}

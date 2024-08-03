package com.imcys.bilias.feature.merge.danmaku.model

/** 弹幕类型 */
enum class DanmakuType {
    Ordinary, // 普通弹幕
    Top, // 顶部弹幕
    Bottom, // 底部弹幕
    Reverse, // 逆向弹幕
    ;

    companion object {
        fun valueOf(type: Int): DanmakuType = when (type) {
            1, 2, 3 -> Ordinary
            4 -> Bottom
            5 -> Top
            6 -> Reverse
            else -> Ordinary
        }
    }
}

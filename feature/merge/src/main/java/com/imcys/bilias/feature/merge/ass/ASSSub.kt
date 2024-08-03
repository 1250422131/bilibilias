package com.imcys.bilias.feature.merge.ass

import com.imcys.bilias.feature.merge.ass.model.buildDialogue
import com.imcys.bilias.feature.merge.ass.model.buildScriptInfo
import com.imcys.bilias.feature.merge.ass.model.buildStyles
import com.imcys.bilias.feature.merge.danmaku.model.Danmaku
import com.imcys.bilias.feature.merge.danmaku.model.DanmakuType

class ASSSub {
    fun gen(title: String, width: Int, height: Int) {
        val info = buildScriptInfo(title, width, height)
        val styles = buildStyles()

    }

    fun genDialogue(pool: List<Danmaku>) {
        var oldType = DanmakuType.Ordinary
        for (danmaku in pool) {
            val time = danmaku.time
            val startTime = startTime(time)
            val endTime = endTime(time)
            val type = danmaku.type
            effect(oldType, type, danmaku.color)
            oldType = type
            buildDialogue(startTime, endTime, danmaku.fontSize, "", danmaku.text)
        }
    }

    /**
     * todo 不知道怎么写了
     */
    @OptIn(ExperimentalStdlibApi::class)
    private fun effect(oldType: DanmakuType, current: DanmakuType, color: Long) {
        val c = "\\c${color.toHexString(colorFormat)}"
        if (oldType == DanmakuType.Top && current == DanmakuType.Top) {

        } else if (current == DanmakuType.Bottom) {

        } else {

        }
    }

    private fun startTime(seconds: Long): String {
        return conversionTime(seconds)
    }

    private fun endTime(seconds: Long): String {
        return conversionTime(seconds + 6)
    }

    private fun conversionTime(seconds: Long): String {
        val second = seconds % 60
        val minute = (seconds / 60) % 60
        val hour = seconds / (60 * 60)
        return "%01d:%02d:%02d".format(hour, minute, second)
    }
}
package com.imcys.bilibilias.tool.danmaku

import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

class DialogueFormat @AssistedInject constructor(@Assisted private val danmaku: Danmaku) : IASSDialogue {
    /**
     * 生成 Dialogue
     * \c&H1200E7
     * "Dialogue: 3,$startTime,$endTime,$fontStyle,,0000,0000,0000,,${positionInfo}${textContent}\n"
     */
    @Suppress("ArgumentListWrapping")
    override fun dialogue(): String {
        return """
            Dialogue: 0,${sendTime()},${endTime()},${fontStyle()},,0000,0000,0000,,${position()}${content()}${'\n'}
        """.trimIndent()
    }
    override fun fontStyle(): String {
        val fontSize = when (danmaku.fontSize) {
            "18" -> "Small"
            "25" -> "Medium"
            "36" -> "Large"
            else -> "Medium"
        }
        return fontSize
    }

    /**
     * \c&H1200E7
     */
    override fun textColor(): String {
        val color = textColors[danmaku.color]
        val textColor = if (color == null) {
            "\\c&HFFFFFF"
        } else {
            "\\$color"
        }
        return textColor
    }

    override fun position(): String {
        var oldPosition = 0
        val position = when (type()) {
            DanmakuType.Top -> {
                oldPosition += 30
                "{\\a6\\pos(960,${(oldPosition)})${textColor()}}"
            }

            DanmakuType.Bottom -> "{\\a6\\pos(960,1050)${textColor()}}"
            else -> {
                // 产生一个随机Y轴位置
                val y = (0..18).random() * 60
                val starX = (2150..2400).random()
                // 确保前后经过了1920
                "{\\move($starX,$y,${1920 - starX},$y)${textColor()}}"
            }
        }
        return position
    }

    override fun content(): String {
        return danmaku.text
    }

    override fun sendTime(): String {
        return formatSeconds(danmaku.sendTime.toLong())
    }

    override fun endTime(): String {
        return formatSeconds(danmaku.sendTime.toLong() + scrollTime)
    }

    override fun type(): DanmakuType {
        return when (danmaku.type) {
            "1", "2", "3" -> DanmakuType.Ordinary
            "4" -> DanmakuType.Bottom
            "5" -> DanmakuType.Top
            "6" -> DanmakuType.Reverse
            else -> DanmakuType.Ordinary
        }
    }

    private fun formatSeconds(seconds: Long): String {
        return converter(seconds)
    }

    private fun converter(seconds: Long): String {
        val hours = seconds / 3600
        val minutes = (seconds % 3600) / 60
        val remainingSeconds = seconds % 60
        return "%01d:%02d:%02s".format(hours, minutes, remainingSeconds)
    }

    companion object {
        private val textColors = mapOf(
            "16646914" to "c&HFE0302",
            "16740868" to "c&HFF7204",
            "16755202" to "c&HFFAA02",
            "16765698" to "c&HFFD302",
            "16776960" to "c&HFFFF00",
            "10546688" to "c&HA0EE00",
            "52480" to "c&H00CD00",
            "104601" to "c&H019899",
            "4351678" to "c&H4266BE",
            "9022215" to "c&H89D5FF",
            "13369971" to "c&HCC0273",
            "2236962" to "c&H222222",
            "10197915" to "c&H9B9B9B",
            "16777215" to "c&HFFFFFF",
        )
    }
}

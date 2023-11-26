package com.imcys.bilibilias.tool.danmaku

interface IASSDialogue {
    val scrollTime: Int
        get() = 12

    fun fontStyle(): String
    fun textColor(): String
    fun position(): String
    fun content(): String
    fun sendTime(): String
    fun endTime(): String
    fun type(): DanmakuType
    fun dialogue(): String
}

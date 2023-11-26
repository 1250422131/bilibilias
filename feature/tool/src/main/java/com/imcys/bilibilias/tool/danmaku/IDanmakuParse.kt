package com.imcys.bilibilias.tool.danmaku

import org.xmlpull.v1.XmlPullParserException
import java.io.File
import java.io.IOException
import java.io.InputStream

interface IDanmakuParse {
    /**
     * todo 两个接口功能重复了
     */
    @Throws(XmlPullParserException::class, IOException::class)
    fun parse(inputStream: InputStream): List<Danmaku>

    fun danmakuFrom(file: File): List<Danmaku>
}

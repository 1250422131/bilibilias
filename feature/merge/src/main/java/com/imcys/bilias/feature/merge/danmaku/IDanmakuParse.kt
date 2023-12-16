package com.imcys.bilias.feature.merge.danmaku

import com.imcys.bilias.feature.merge.danmaku.model.Danmaku
import org.xmlpull.v1.XmlPullParserException
import java.io.File
import java.io.IOException
import java.io.InputStream

interface IDanmakuParse {
    @Throws(XmlPullParserException::class, IOException::class)
    fun parse(inputStream: InputStream): List<Danmaku>

    fun parse(file: File): List<Danmaku>
}

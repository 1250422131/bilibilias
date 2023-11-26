package com.imcys.bilibilias.tool.danmaku

import android.util.Xml
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserException
import timber.log.Timber
import java.io.File
import java.io.IOException
import java.io.InputStream
import javax.inject.Inject

/**
 * xml弹幕转换ass弹幕
 *
 * 参考 https://github.com/SocialSisterYi/bilibili-API-collect/blob/master/docs/danmaku/danmaku_xml.md
 */
class DanmakuXMLParse @Inject constructor() : IDanmakuParse {

    private val ns: String? = null
    override fun danmakuFrom(file: File): List<Danmaku> {
        return try {
            parse(file.inputStream())
        } catch (e: Exception) {
            Timber.d(e)
            emptyList()
        }
    }

    @Throws(XmlPullParserException::class, IOException::class)
    override fun parse(inputStream: InputStream): List<Danmaku> {
        inputStream.use { stream ->
            val parser: XmlPullParser = Xml.newPullParser()
            parser.setInput(stream, null)
            parser.nextTag()
            return readDanmaku(parser)
        }
    }

    @Throws(XmlPullParserException::class, IOException::class)
    private fun readDanmaku(parser: XmlPullParser): List<Danmaku> {
        val danmakuList = mutableListOf<Danmaku>()

        parser.require(XmlPullParser.START_TAG, ns, "i")
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.eventType != XmlPullParser.START_TAG) {
                continue
            }
            if (parser.name == "d") {
                val content = readContent(parser)
                danmakuList.add(content)
            } else {
                Timber.d("跳过")
                skip(parser)
            }
        }
        return danmakuList
    }

    @Throws(XmlPullParserException::class, IOException::class)
    private fun readContent(parser: XmlPullParser): Danmaku {
        val att = readAttribute(parser)
        // 解析属性
        val (time, type, triple) = parseAttributes(att)
        val (fontSize, color, sendTime) = triple
        val text = readText(parser)
        return Danmaku(time, type, fontSize, text, color, sendTime)
    }

    private fun parseAttributes(att: String): Triple<String, String, Triple<String, String, String>> {
        val content = att.split(',')
        val time = content[0]
        val type = content[1]
        val size = content[2]
        val color = content[3]
        val sendTime = content[4]
        return Triple(time, type, Triple(size, color, sendTime))
    }

    @Throws(IOException::class, XmlPullParserException::class)
    private fun readText(parser: XmlPullParser): String {
        var result = ""
        Timber.d("event=${parser.eventType}")
        if (parser.eventType == XmlPullParser.TEXT) {
            result = parser.text
            parser.nextTag()
        }
        return result
    }

    @Throws(IOException::class, XmlPullParserException::class)
    private fun readAttribute(parser: XmlPullParser): String {
        val att: String = parser.getAttributeValue(ns, "p") ?: ""
        parser.nextToken()
        return att
    }

    @Throws(XmlPullParserException::class, IOException::class)
    private fun skip(parser: XmlPullParser) {
        if (parser.eventType != XmlPullParser.START_TAG) {
            throw IllegalStateException()
        }
        var depth = 1
        while (depth != 0) {
            when (parser.next()) {
                XmlPullParser.END_TAG -> depth--
                XmlPullParser.START_TAG -> depth++
            }
        }
    }
}

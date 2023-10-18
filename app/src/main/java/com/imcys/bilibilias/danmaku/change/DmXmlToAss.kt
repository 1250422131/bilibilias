package com.imcys.bilibilias.danmaku.change

import android.content.Context
import com.imcys.bilibilias.danmaku.enmu.DanmakuType
import org.w3c.dom.Element
import org.w3c.dom.Node
import org.xml.sax.InputSource
import java.io.StringReader
import javax.xml.parsers.DocumentBuilderFactory

/**
 * xml弹幕转换ass弹幕
 */
object DmXmlToAss {
    // 参考 https://github.com/SocialSisterYi/bilibili-API-collect/blob/master/docs/danmaku/danmaku_xml.md
    private val textColors = mapOf(
        "16646914" to "FE0302",
        "16740868" to "FF7204",
        "16755202" to "FFAA02",
        "16765698" to "FFD302",
        "16776960" to "FFFF00",
        "10546688" to "A0EE00",
        "52480" to "00CD00",
        "104601" to "019899",
        "4351678" to "4266BE",
        "9022215" to "89D5FF",
        "13369971" to "CC0273",
        "2236962" to "222222",
        "10197915" to "9B9B9B",
        "16777215" to "FFFFFF",
    )

    fun xmlToAss(
        xmlString: String,
        title: String,
        playResX: String,
        playResY: String,
        context: Context,
    ): String {
        // 组装头部信息
        val headersInfo = CCJsonToAss.buildHeadersInfo(title, playResX, playResY)
        val fontStyleInfo = CCJsonToAss.getFontStyleInfo()
        val danmakuInfo = buildDanmakuInfo(xmlString)

        // 封装身体
        return headersInfo + "\n$fontStyleInfo\n\n" + danmakuInfo
    }

    /**
     * 构建弹幕部分
     */
    private fun buildDanmakuInfo(xmlString: String): String {
        var danmakus = CCJsonToAss.getFormat() + "\n"
        val dbf = DocumentBuilderFactory.newInstance()
        val db = dbf.newDocumentBuilder()
        val inputSource = InputSource(StringReader(xmlString))
        val xmlDoc = db.parse(inputSource)

        val lis = xmlDoc.getElementsByTagName("i")
        if (lis.length == 0) return danmakus
        // 看看它下面还有没有了
        val lisNode = lis.item(0)
        if (lisNode.nodeType == Node.ELEMENT_NODE) {
            // 拿到全部的弹幕元素
            val ds = (lisNode as Element).getElementsByTagName("d")
            // 遍历每一条

            var oldDmPosition = 0 // 旧的弹幕位置
            var oldDmType = DanmakuType.BottomDanmaku // 旧的弹幕类型

            for (i in 0 until ds.length) {
                val danmakuElement = ds.item(i)
                // 解析弹幕
                // 拿到弹幕信息
                val infoStr = danmakuElement.attributes.getNamedItem("p").textContent
                val danmakuInfos = infoStr.split(",")

                val startTime = CCJsonToAss.formatSeconds(danmakuInfos[0].toDouble())
                // 6秒延迟
                var endTime = CCJsonToAss.formatSeconds(danmakuInfos[0].toDouble() + 12.5)

                // 字号
                val fontStyle = when (danmakuInfos[2]) {
                    "18" -> "Small"
                    "25" -> "Medium"
                    "36" -> "Large"
                    else -> "Medium"
                }

                // 字体颜色
                val textColor = if (textColors[danmakuInfos[3]] == "FFFFFF") {
                    "\\c&HFFFFFF"
                } else {
                    "\\c&H${textColors[danmakuInfos[3]]}\\3c&HFFFFFF"
                }

                val positionInfo =
                    if (danmakuInfos[1] == "5" && oldDmType == DanmakuType.TopDanmaku) {
                        // 这种情况下就是说顶部弹幕连起来了
                        oldDmPosition += 30
                        endTime = CCJsonToAss.formatSeconds(danmakuInfos[0].toDouble() + 4)
                        "{\\a6\\pos(960,${(oldDmPosition)})$textColor}"
                    } else if (danmakuInfos[1] == "4") {
                        endTime = CCJsonToAss.formatSeconds(danmakuInfos[0].toDouble() + 4)
                        "{\\a6\\pos(960,1050)$textColor}"
                    } else {
                        // 产生一个随机Y轴位置
                        val y = (0..18).random() * 60
                        val starX = (2150..2400).random()
                        // 清除连续
                        oldDmPosition = 0
                        // 确保前后经过了1920
                        "{\\move($starX,$y,${1920 - starX},$y)\\c$textColor}"
                    }

                // 记录本次弹幕类型
                oldDmType = when (danmakuInfos[1]) {
                    "1", "2", "3" -> DanmakuType.OrdinaryDanmaku
                    "5" -> DanmakuType.TopDanmaku
                    "4" -> DanmakuType.BottomDanmaku
                    "6" -> DanmakuType.ReverseDanmaku
                    else -> DanmakuType.OrdinaryDanmaku
                }

                val textContent = danmakuElement.textContent

                danmakus += "Dialogue: 3,$startTime,$endTime,$fontStyle,,0000,0000,0000,,${positionInfo}${textContent}\n"
            }
        }

        return danmakus
    }
}

package com.imcys.bilibilias.danmaku.change

import android.content.Context
import android.util.Log
import com.imcys.bilibilias.R
import com.imcys.bilibilias.danmaku.enmu.DanmakuType
import org.w3c.dom.Element
import org.w3c.dom.Node
import org.xml.sax.InputSource
import java.io.StringReader
import java.text.DecimalFormat
import java.util.concurrent.TimeUnit
import javax.xml.parsers.DocumentBuilderFactory
import kotlin.math.log


/**
 * xml弹幕转换ass弹幕
 */
object DmXmlToAss {


    //参考
    //https://github.com/SocialSisterYi/bilibili-API-collect/blob/master/docs/danmaku/danmaku_xml.md
    private val textColors = mutableMapOf(
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
        context: Context
    ): String {

        //组装头部信息
        val headersInfo = buildHeadersInfo(title, playResX, playResY, context)

        val fontStyleInfo = context.getString(R.string.ass_font_info_model)
        val danmakuInfo = buildDanmakuInfo(xmlString, context)

        //封装身体
        return headersInfo + "\n$fontStyleInfo\n\n" + danmakuInfo

    }

    /**
     * 构建弹幕部分
     */
    private fun buildDanmakuInfo(xmlString: String, context: Context): String {

        //使用原生的XML解析
        Log.e("测试",xmlString)
        var danmakus = context.getString(R.string.ass_events_info_model) + "\n"
        //拿到xml文档对象
        val dbf = DocumentBuilderFactory.newInstance()
        val db = dbf.newDocumentBuilder()
        val inputSource = InputSource(StringReader(xmlString))
        val xmlDoc = db.parse(inputSource)


        val lis = xmlDoc.getElementsByTagName("i")
        if (lis.length > 0) {
            //看看它下面还有没有了
            val lisNode = lis.item(0)
            if (lisNode.nodeType == Node.ELEMENT_NODE) {
                //拿到全部的弹幕元素
                val ds = (lisNode as Element).getElementsByTagName("d")
                //遍历每一条

                var oldDmPosition = 0 //旧的弹幕位置
                var oldDmType = DanmakuType.BottomDanmaku //旧的弹幕类型

                for (i in 0 until ds.length) {
                    val danmakuElement = ds.item(i)
                    //解析弹幕
                    //拿到弹幕信息
                    val infoStr = danmakuElement.attributes.getNamedItem("p").textContent
                    val danmakuInfos = infoStr.split(",")

                    val startTime = formatSeconds(danmakuInfos[0].toDouble())
                    //6秒延迟
                    val endTime = formatSeconds(danmakuInfos[0].toDouble() + 6)

                    //字号
                    val fontStyle = when (danmakuInfos[2]) {
                        "18" -> "Small"
                        "25" -> "Medium"
                        "36" -> "Large"
                        else -> "Medium"
                    }

                    //字体颜色
                    val textColor = "\\c&H${textColors[danmakuInfos[3]]}&"

                    val positionInfo =
                        if (danmakuInfos[1] == "5" && oldDmType == DanmakuType.TopDanmaku) {
                            //这种情况下就是说顶部弹幕连起来了
                            oldDmPosition += 30
                            "{\\pos(960,${(oldDmPosition)})$textColor}"
                        } else if (danmakuInfos[1] == "4") {
                            "{\\pos(960,1050)$textColor}"
                        } else {
                            //产生一个随机Y轴位置
                            val y = (0 until 18).random() * 60
                            val starX = (2000..2400).random()
                            //清除连续
                            oldDmPosition = 0
                            //确保前后经过了1920
                            "{\\move($starX,$y,${1920 - starX},$y,0,6000)\\c$textColor}"
                        }

                    //记录本次弹幕类型
                    oldDmType = when (danmakuInfos[1]) {
                        "1", "2", "3" -> DanmakuType.OrdinaryDanmaku
                        "5" -> DanmakuType.TopDanmaku
                        "4" -> DanmakuType.BottomDanmaku
                        "6" -> DanmakuType.ReverseDanmaku
                        else -> DanmakuType.OrdinaryDanmaku
                    }

                    val textContent = danmakuElement.textContent

                    danmakus += "Dialogue: 0,$startTime,$endTime,$fontStyle,,0,0,0,,${positionInfo}${textContent}\n"

                }

            }


        }

        return danmakus


    }

    /**
     * 时间格式化
     */
    private fun formatSeconds(seconds: Double): String {
        val formatter = DecimalFormat("00.00")
        val hours = TimeUnit.SECONDS.toHours(seconds.toLong())
        val minutes = TimeUnit.SECONDS.toMinutes(seconds.toLong()) % 60
        val remainingSeconds =
            seconds - TimeUnit.HOURS.toSeconds(hours) - TimeUnit.MINUTES.toSeconds(minutes)
        return String.format("%d:%02d:%s", hours, minutes, formatter.format(remainingSeconds))
    }


    /**
     * 构筑头部信息
     */
    private fun buildHeadersInfo(
        title: String,
        playResX: String,
        playResY: String,
        context: Context
    ) = context.run {
        getString(R.string.ass_script_info_model) + "\n" +
                "Title: $title\n" +
                "ScriptType: v4.00+\n" +
                "PlayResX:$playResX \n" +
                "PlayResY:$playResY \n" +
                "Timer: 10.0000\n" +
                "WrapStyle: 2\n" +
                "ScaledBorderAndShadow: no\n\n"
    }

}
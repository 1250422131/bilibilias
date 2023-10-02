package com.imcys.bilibilias.danmaku.change

import android.content.Context
import com.imcys.bilibilias.R
import com.imcys.bilibilias.home.ui.model.VideoCCInfo
import java.text.DecimalFormat
import java.util.concurrent.TimeUnit

/**
 * CC字母在JSON格式时转换为
 */
object CCJsonToAss {


    /**
     * 构筑头部信息
     */
    private fun buildHeadersInfo(
        title: String,
        playResX: String,
        playResY: String,
        context: Context,
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


    fun jsonToAss(
        videoCCInfo: VideoCCInfo,
        title: String,
        playResX: String,
        playResY: String,
        context: Context,
    ): String {
        // 组装头部信息
        val headersInfo = buildHeadersInfo(title, playResX, playResY, context)

        val fontStyleInfo = context.getString(R.string.ass_font_info_model)
        val ccInfo = buildCCInfo(videoCCInfo, context)

        // 封装身体
        return headersInfo + "\n$fontStyleInfo\n\n" + ccInfo
    }

    /**
     * 构建字幕体
     */
    private fun buildCCInfo(videoCCInfo: VideoCCInfo, context: Context): String {
        var danmakus = context.getString(R.string.ass_events_info_model) + "\n"
        videoCCInfo.body.forEach {
            val startTime = formatSeconds(it.from)
            // 6秒延迟
            val endTime = formatSeconds(it.to)

            // 字号
            val fontStyle = "Large"

            val textColor = "\\c&H000000&"

            val positionInfo = "{\\pos(960,1050)$textColor}"

            val textContent = it.content

            danmakus += "Dialogue: 0,$startTime,$endTime,$fontStyle,,0,0,0,,${positionInfo}${textContent}\n"

        }

        return danmakus;

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


}
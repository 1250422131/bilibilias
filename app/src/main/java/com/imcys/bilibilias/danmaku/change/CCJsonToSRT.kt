package com.imcys.bilibilias.danmaku.change

import com.imcys.bilibilias.home.ui.model.VideoCCInfo

object CCJsonToSrt {

    fun jsonToSrt(videoCCInfo: VideoCCInfo): String {
        val srtBuilder = StringBuilder()
        videoCCInfo.body.forEachIndexed { index, it ->
            val startTime = formatSrtTime(it.from)
            val endTime = formatSrtTime(it.to)
            srtBuilder.append("${index + 1}\n")
            srtBuilder.append("$startTime --> $endTime\n")
            srtBuilder.append("${it.content}\n\n")
        }
        return srtBuilder.toString().trim()
    }

    private fun formatSrtTime(seconds: Double): String {
        val hours = (seconds / 3600).toInt()
        val minutes = ((seconds % 3600) / 60).toInt()
        val secs = seconds % 60
        val millis = ((secs - secs.toInt()) * 1000).toInt()
        return String.format("%02d:%02d:%02d,%03d", hours, minutes, secs.toInt(), millis)
    }
}
package com.imcys.bilibilias.base.extend

import android.graphics.Color
import android.os.Build
import android.text.Html
import android.text.Html.FROM_HTML_MODE_COMPACT
import androidx.core.graphics.toColorInt

/**
 * Html化
 * @receiver String 待Html解析的字符串
 * @return String Html解析后的字符串
 */

fun String.toHtml(): String {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        Html.fromHtml(this, FROM_HTML_MODE_COMPACT).toString()
    } else {
        Html.fromHtml(this).toString()
    }
}

fun String.toColorInt(): Int = Color.parseColor(this)


/**
 * 取中间字符串，需要提供开始截取的位置和结束截取的位置
 * @receiver String
 * @param startString String
 * @param endString String
 * @return String
 */
fun String.extract(startString: String, endString: String): String {
    return this.substring(this.indexOf(startString) + startString.length ,
        this.indexOf(endString))
}






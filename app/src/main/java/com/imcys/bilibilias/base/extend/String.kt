package com.imcys.bilibilias.base.extend

import android.os.Build
import android.text.Html
import android.text.Html.FROM_HTML_MODE_COMPACT

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




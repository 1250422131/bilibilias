package com.imcys.bilibilias.common.base.extend

import android.annotation.SuppressLint
import java.text.SimpleDateFormat
import java.util.*

/**
 * 时间戳转换成字符窜
 * @param pattern 时间样式 yyyy-MM-dd HH:mm:ss
 * @return [String] 时间字符串
 */
@SuppressLint("SimpleDateFormat")
fun Long.timeStampDateStr(pattern: String = "yyyy-MM-dd HH:mm:ss"): String {
    val date = Date(this * 1000)
    val format = SimpleDateFormat(pattern)
    return format.format(date)
}

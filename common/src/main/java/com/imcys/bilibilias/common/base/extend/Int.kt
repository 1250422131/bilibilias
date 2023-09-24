package com.imcys.bilibilias.common.base.extend

import kotlin.math.abs
import kotlin.math.log10

fun Int.digitalConversion(): String {
    if (this < 10000) return toString()
    val originallyNum = this.toString()
    val sb = StringBuilder()
    when (this.length()) {
        5 -> sb.append(originallyNum[0])

        6 -> sb.append(originallyNum.substring(0, 2))

        7 -> sb.append(originallyNum.substring(0, 3))

        8 -> sb.append(originallyNum.substring(0, 4))

        else -> sb.append(originallyNum[0])
    }
    sb.append('.')
    if (this.length() > 8) return sb.append(originallyNum[1]).append("亿").toString()
    return sb.append(this.length() - 4).append("万").toString()
}

fun Int.length() = when (this) {
    0 -> 1
    else -> log10(abs(toDouble())).toInt() + 1
}

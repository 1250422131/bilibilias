package com.imcys.bilibilias.common.base.utils

object AsVideoNumUtils {
    fun getQualityName(code: Int): String {
        return when (code) {
            30216 -> "64K"
            30232 -> "132K"
            30250 -> "杜比全景声"
            30251 -> "Hi-Res无损"
            30280 -> "192K"
            else -> "192K"
        }
    }
}

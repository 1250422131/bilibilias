package com.imcys.bilibilias.common.base.utils

object AsVideoNumUtils {

    fun getBvid(text: String): String? {
        if (isBV(text)) {
            return Regex.bvRegex.find(text)?.value
        }
        return null
    }
    fun isBV(text: String) = Regex.bvRegex.containsMatchIn(text)
    fun isAV(text: String) = Regex.avRegex.containsMatchIn(text)
    fun getAvid(text: String): String? {
        if (isAV(text)) {
            return Regex.avRegex.find(text)?.value
        }
        return null
    }

    fun getQualityName(code: Int): String {
        return when (code) {
            30216 -> "64K"
            30232 -> "132K"
            30250 -> "杜比全景声"
            30251 -> "Hi-Res无损"
            30280 -> "192K"
            else -> {
                "192K"
            }
        }
    }
}

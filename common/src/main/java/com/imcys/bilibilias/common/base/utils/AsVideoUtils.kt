package com.imcys.bilibilias.common.base.utils

object AsVideoUtils {

    /**
     * 是否是番剧
     */
    fun isEp(text: String) = RegexUtils.epRegex.containsMatchIn(text)
    fun isBVStart(text: String) = RegexUtils.bvRegex.containsMatchIn(text)
    fun isBVHttp(text: String) = RegexUtils.bvHttpRegex.containsMatchIn(text)
    fun isAV(text: String) = RegexUtils.avRegex.containsMatchIn(text)
    fun getEpid(text: String): String? {
        if (isEp(text)) {
            return RegexUtils.epRegex.find(text)?.value
        }
        return null
    }

    fun getBvHttp(text: String): String? {
        if (isBVHttp(text)) {
            return RegexUtils.bvHttpRegex.find(text)?.value
        }
        return null
    }

    fun getBvid(text: String): String? {
        if (isBVStart(text)) {
            return RegexUtils.bvRegex.find(text)?.value
        }
        return null
    }

    fun getAvid(text: String): String? {
        if (isAV(text)) {
            return RegexUtils.avRegex.find(text)?.value
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
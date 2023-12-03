package com.imcys.common.utils

object AsVideoUtils {

    /**
     * 是否是番剧
     */
    fun isEp(text: String) = RegexUtils.epRegex.containsMatchIn(text)
    fun isBV(text: String) = RegexUtils.bvRegex.containsMatchIn(text)
    fun isShortLink(text: String) = RegexUtils.shortLink.containsMatchIn(text)
    fun isAV(text: String) = RegexUtils.avRegex.containsMatchIn(text)

    fun isResolvable(text: String): Boolean = isEp(text) || isBV(text) || isAV(text) || isShortLink(text)
    fun getEpid(text: String): String? {
        if (isEp(text)) {
            return RegexUtils.epRegex.find(text)?.value
        }
        return null
    }

    fun getShortLink(text: String): String? {
        if (isShortLink(text)) {
            return RegexUtils.shortLink.find(text)?.value
        }
        return null
    }

    fun getBvid(text: String): String? {
        if (isBV(text)) {
            return "BV" + RegexUtils.bvRegex.find(text)?.value
        }
        return null
    }

    fun getAid(text: String): String? {
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
            else -> "192K"
        }
    }
}

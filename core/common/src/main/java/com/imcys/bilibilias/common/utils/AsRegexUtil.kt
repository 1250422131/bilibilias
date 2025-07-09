package com.imcys.bilibilias.common.utils


/**
 * AS解析工具类：
 * 支持AcFun，迁移自V2.x
 */
object AsRegexUtil {
    private val regex1 = Regex("""(?:^|/)ep([0-9]+)""")
    private val regex2 = Regex("""https://b23.tv/(([A-z]+|\d+){6,})""")
    private val regex3 = Regex("""https://space.bilibili.com/?(\d+).*""")
    private val regex4 = Regex("""(?<=BV|Bv|bv|bV)[A-Za-z0-9]{10}""")
    private val regex5 = Regex("""(?<=(av|aV|AV|Av))([0-9]+)""")
    private val regex6 = Regex("""https://bili2233.cn/(([A-z]+|\d+){6,})""")
    private fun isBV(text: String) = regex4.containsMatchIn(text)
    private fun isAV(text: String) = regex5.containsMatchIn(text)
    private fun isEP(text: String) = regex1.containsMatchIn(text)
    private fun isShortLink(text: String) = text.contains("""https://b23.tv""")
    private fun is2233ShortLink(text: String) = text.contains("""https://bili2233.cn""")
    private fun isUserSpace(text: String) = text.startsWith("""https://space.bilibili.com""")
    fun parse(text: String): TextType? {
        return when {
            isBV(text) -> regex4.find(text)?.value?.let { TextType.BILI.BV("BV$it") }
            isAV(text) -> regex5.find(text)?.groupValues?.get(2)
                ?.let { TextType.BILI.AV(it.toLong()) }

            isEP(text) -> regex1.find(text)?.groupValues?.get(1)
                ?.let { TextType.BILI.EP(it.toLong()) }

            isShortLink(text) -> regex2.find(text)?.groupValues?.get(1)
                ?.let { TextType.BILI.ShortLink("https://b23.tv/$it") }

            is2233ShortLink(text) -> regex6.find(text)?.groupValues?.get(1)
                ?.let { TextType.BILI.ShortLink("https://bili2233.cn/$it") }

            isUserSpace(text) -> regex3.find(text)?.groupValues?.get(1)
                ?.let { TextType.BILI.UserSpace(it) }

            else -> null
        }
    }
}

sealed interface TextType {
    sealed interface BILI : TextType {
        data class BV(val text: String) : BILI
        data class AV(val text: Long) : BILI
        data class EP(val text: Long) : BILI
        data class ShortLink(val text: String) : BILI
        data class UserSpace(val text: String) : BILI
    }

}

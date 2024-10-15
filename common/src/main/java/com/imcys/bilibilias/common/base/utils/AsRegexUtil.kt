package com.imcys.bilibilias.common.base.utils

object AsRegexUtil {
    private val regex1 = Regex("""(?:^|/)ep([0-9]+)""")
    private val regex2 = Regex("""https://b23.tv/([A-z]|\d)*""")
    private val regex3 = Regex("""https://space.bilibili.com/?(\d+).*""")
    private val regex4 = Regex("""(?<=BV|Bv|bv|bV)[A-Za-z0-9]{10}""")
    private val regex5 = Regex("""(?<=(av|aV|AV|Av))([0-9]+)""")
    private fun isBV(text: String) = regex4.containsMatchIn(text)
    private fun isAV(text: String) = regex5.containsMatchIn(text)
    private fun isEP(text: String) = regex1.containsMatchIn(text)
    private fun isShortLink(text: String) = text.startsWith("""https://b23.tv""")
    private fun isUserSpace(text: String) = text.startsWith("""https://space.bilibili.com""")
    fun parse(text: String): TextType? {
        return when {
            isBV(text) -> regex4.find(text)?.value?.let { TextType.BV("BV$it") }
            isAV(text) -> regex5.find(text)?.value?.let { TextType.AV(it.toLong()) }
            isEP(text) -> regex1.find(text)?.value?.let { TextType.EP(it.toLong()) }
            isShortLink(text) -> regex2.find(text)?.value?.let { TextType.ShortLink(it) }
            isUserSpace(text) -> regex3.find(text)?.value?.let { TextType.UserSpace(it) }
            else -> null
        }
    }
}

sealed interface TextType {
    data class BV(val text: String) : TextType
    data class AV(val text: Long) : TextType
    data class EP(val text: Long) : TextType
    data class ShortLink(val text: String) : TextType
    data class UserSpace(val text: String) : TextType
}
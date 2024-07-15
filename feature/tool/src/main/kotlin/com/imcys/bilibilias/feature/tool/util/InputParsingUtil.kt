package com.imcys.bilibilias.feature.tool.util

object InputParseUtil {
    private val epRegex by lazy(LazyThreadSafetyMode.NONE) { Regex("""(?<=ep|eP|Ep|EP)([0-9]+)""") }

    // bv过滤
    private val bvRegex by lazy(LazyThreadSafetyMode.NONE) {
        Regex("""(?<=BV|Bv|bv|bV)[A-Za-z0-9]{10}""")
    }

    private val shortLink by lazy(LazyThreadSafetyMode.NONE) {
        Regex("""https://b23.tv/[a-zA-Z0-9]+""")
    }

    // av过滤
    private val avRegex by lazy(LazyThreadSafetyMode.NONE) { Regex("""(?<=(av|aV|Av|AV))([0-9]+)""") }

    /**
     * 是否是番剧
     */
    fun isEp(text: String) = epRegex.containsMatchIn(text)
    fun isBV(text: String) = bvRegex.containsMatchIn(text)
    fun isShortLink(text: String) = shortLink.containsMatchIn(text)
    fun isAV(text: String) = avRegex.containsMatchIn(text)

    @Suppress("ReturnCount")
    fun searchType(text: String): SearchType {
        getBvid(text)?.let { return SearchType.BV(it) }
        getEpid(text)?.let { return SearchType.EP(it) }
        getAid(text)?.let { return SearchType.AV(it) }
        getShortLink(text)?.let { return SearchType.ShortLink(it) }
        return SearchType.None
    }

    fun isResolvable(text: String): Boolean =
        isEp(text) || isBV(text) || isAV(text) || isShortLink(text)

    fun getEpid(text: String): String? {
        if (isEp(text)) {
            return epRegex.find(text)?.value
        }
        return null
    }

    fun getShortLink(text: String): String? {
        if (isShortLink(text)) {
            return shortLink.find(text)?.value
        }
        return null
    }

    fun getBvid(text: String): String? {
        if (isBV(text)) {
            return "BV" + bvRegex.find(text)?.value
        }
        return null
    }

    fun getAid(text: String): String? {
        if (isAV(text)) {
            return avRegex.find(text)?.value
        }
        return null
    }
}

sealed interface SearchType {
    data object None : SearchType
    data class BV(val id: String) : SearchType
    data class AV(val id: String) : SearchType
    data class EP(val id: String) : SearchType

    data class ShortLink(val url: String) : SearchType
}

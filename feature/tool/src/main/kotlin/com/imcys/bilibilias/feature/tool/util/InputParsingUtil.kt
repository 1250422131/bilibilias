package com.imcys.bilibilias.feature.tool.util

// todo need test
object InputParseUtil {
    private val epRegex by lazy(LazyThreadSafetyMode.NONE) { Regex("""(?<=ep)([0-9]+)""") }

    // bv过滤
    private val bvRegex by lazy(LazyThreadSafetyMode.NONE) {
        Regex("""(?<=BV|Bv|bv|bV)[A-Za-z0-9]{10}""")
    }

    private val shortLink by lazy(LazyThreadSafetyMode.NONE) {
        Regex("""https://b23.tv/[a-zA-Z0-9]+""")
    }

    // av过滤
    private val avRegex by lazy(LazyThreadSafetyMode.NONE) { Regex("""(?<=(av))([0-9]+)""") }

    /**
     * 是否是番剧
     */
    fun isEp(text: String) = epRegex.containsMatchIn(text)
    fun isBV(text: String) = bvRegex.containsMatchIn(text)
    fun isShortLink(text: String) = shortLink.containsMatchIn(text)
    fun isAV(text: String) = avRegex.containsMatchIn(text)
    fun searchType(text: String): SearchType {
        val bv = getBvid(text)
        if (bv != null) {
            return SearchType.BV(bv)
        }
        val ep = getEpid(text)
        if (ep != null) {
            return SearchType.EP(ep)
        }
        val aid = getAid(text)
        if (aid != null) {
            return SearchType.AV(aid)
        }
        val link = getShortLink(text)
        if (link != null) {
            return SearchType.ShortLink(link)
        }
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

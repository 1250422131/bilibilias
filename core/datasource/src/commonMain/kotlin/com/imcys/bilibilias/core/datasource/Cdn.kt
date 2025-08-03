package com.imcys.bilibilias.core.datasource

import io.ktor.http.parseUrl

/**
 * https://github.com/the1812/Bilibili-Evolved/issues/3234
 * 已知所有 BCache 型、所有 UPOS 型以及部分 MCDN 型的 playurl 可以简单替换 Host 为 Mirror 型。
 */
enum class CdnType {
    MIRROR,
    UPOS,
    BCACHE,
    MCDN;

    companion object {
        fun fromString(typeString: String?): CdnType? { // Return nullable
            return when (typeString?.lowercase()) {
                "mcdn" -> MCDN
                "upos" -> UPOS
                "bcache" -> BCACHE
                "mirror" -> MIRROR
                else -> null
            }
        }
    }
}

data class CdnResource(
    val url: String,
    val type: CdnType,
    val host: String,
)

object Cdn {
    fun parse(urlString: String): CdnResource? {
        val url = parseUrl(urlString) ?: return null

        val typeString = url.parameters["os"]
        val type = CdnType.fromString(typeString) ?: CdnType.MIRROR

        return CdnResource(urlString, type, url.host)
    }
}
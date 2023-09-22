package com.imcys.bilibilias.common.base.utils

object RegexUtils {
    val epRegex by lazy(LazyThreadSafetyMode.NONE) { Regex("""(?<=ep)([0-9]+)""") }

    // bv过滤
    val bvRegex by lazy(
        LazyThreadSafetyMode.NONE
    ) { Regex("""(BV|bv|Bv|bV)([a-z|A-Z|0-9]{10})""", RegexOption.IGNORE_CASE) }

    val bvHttpRegex by lazy(LazyThreadSafetyMode.NONE) {
        Regex("""https://b23.tv/([A-z]|\d)*""")
    }

    // av过滤
    val avRegex by lazy(LazyThreadSafetyMode.NONE) { Regex("""(?<=(av))([0-9]+)""", RegexOption.IGNORE_CASE) }
}

package com.imcys.bilibilias.common.base.utils

object RegexUtils {
    val epRegex by lazy(LazyThreadSafetyMode.NONE) { Regex("""(?<=ep)([0-9]+)""") }

    // bv过滤
    val bvRegex by lazy(
        LazyThreadSafetyMode.NONE
    ) { Regex("""^bv[\da-zA-Z]{10}${'$'}""", RegexOption.IGNORE_CASE) }

    val shortLink by lazy(LazyThreadSafetyMode.NONE) {
        Regex("""https://b23.tv/[a-zA-Z0-9]+${'$'}""")
    }

    // av过滤
    val avRegex by lazy(LazyThreadSafetyMode.NONE) { Regex("""(?<=(av))([0-9]+)""", RegexOption.IGNORE_CASE) }
}

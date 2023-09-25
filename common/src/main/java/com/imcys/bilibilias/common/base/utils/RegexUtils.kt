package com.imcys.bilibilias.common.base.utils

object RegexUtils {
    val epRegex by lazy(LazyThreadSafetyMode.NONE) { Regex("""(?<=ep)([0-9]+)""") }

    // bv过滤
    val bvRegex by lazy(
        LazyThreadSafetyMode.NONE
    ) { Regex("""[BVvb][A-Za-z0-9]{11}""", RegexOption.IGNORE_CASE) }
    val bvRegex1 by lazy(
        LazyThreadSafetyMode.NONE
    ) { Regex("""(?<=BV|Bv|bv|bV)[A-Za-z0-9]{10}""", RegexOption.IGNORE_CASE) }

    val shortLink by lazy(LazyThreadSafetyMode.NONE) {
        Regex("""https://b23.tv/[a-zA-Z0-9]+${'$'}""")
    }

    // av过滤
    val avRegex by lazy(LazyThreadSafetyMode.NONE) { Regex("""(?<=(av))([0-9]+)""", RegexOption.IGNORE_CASE) }
}

package com.imcys.bilibilias.common.base.utils

object RegexUtils {
    val epRegex by lazy(LazyThreadSafetyMode.NONE) { Regex("""(?<=ep)([0-9]+)""") }

    // bv过滤
    val bvRegex by lazy(
        LazyThreadSafetyMode.NONE
    ) { Regex("""(BV|bv|Bv|bV)1([A-z]|[0-9]){2}4([A-z]|[0-9])1([A-z]|[0-9])7([A-z]|[0-9]){2}""") }

    val bvHttpRegex by lazy(LazyThreadSafetyMode.NONE) {
        Regex("""https://b23.tv/([A-z]|\d)*""")
    }

    // av过滤
    val avRegex by lazy(LazyThreadSafetyMode.NONE) { Regex("""(?<=(av|aV|AV|Av))([0-9]+)""") }
}

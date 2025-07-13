package com.imcys.bilibilias.common.utils

fun String.toHttps() = replace(
    "http://",
    "https://"
)
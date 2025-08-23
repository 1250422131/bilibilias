package com.imcys.bilibilias.common.utils

fun String.toHttps() = replace(
    "http://",
    "https://"
)


/**
 * 转化
 * avc1(AVC/h.264)
 * hev1(HEVC/h.265)
 * hvc1(HEVC/h.265)
 * dvh1(HEVC/h.265)
 * av01(AV1)
 */
fun String.toVideoCode() = when (this) {
    "avc1" -> "AVC/h.264"
    "hev1", "hvc1", "dvh1" -> "HEVC/h.265"
    "av01" -> "AV1"
    else -> this
}

fun String.toMenuVideoCode() = when (this) {
    "avc1" -> "AVC/h.264"
    "hev1", "hvc1", "dvh1" -> "HEVC/h.265"
    "av01" -> "AV1"
    else -> this
} + "(${this})"
package com.imcys.bilibilias.core.model

/**
 * 贡献内容
 */
data class DedicateBean(
    val title: String,
    val longTitle: String,
    val doc: String,
    val face: String,
    val link: String = "",
)
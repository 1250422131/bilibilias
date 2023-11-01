package com.imcys.bilibilias.home.ui.model


data class OldHomeAdBean(
    val code: Int, // 0
    val `data`: List<Data>,
) {
    data class Data(
        val longTitle: String, // awa
        val showType: Int, // 1
        val title: String, // 老王梯子
        val content: String,
    )
}
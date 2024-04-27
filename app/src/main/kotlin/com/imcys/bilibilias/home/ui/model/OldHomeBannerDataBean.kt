package com.imcys.bilibilias.home.ui.model

import kotlinx.serialization.Serializable

/**
 * 旧版首页轮播图数据
 */
@Serializable
data class OldHomeBannerDataBean(
    val code: Int = 0,
    val time: Int = 0,
    val imgUrlList: List<String> = emptyList(),
    val textList: List<String> = emptyList(),
    val typeList: List<String> = emptyList(),
    val dataList: List<String> = emptyList(),
    val successToast: List<String> = emptyList(),
    val failToast: List<String> = emptyList(),
    val postData: List<String> = emptyList(),
    val token: List<Int> = emptyList()
)

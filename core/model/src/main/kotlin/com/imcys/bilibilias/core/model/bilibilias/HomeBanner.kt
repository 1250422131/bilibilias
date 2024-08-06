package com.imcys.bilibilias.core.model.bilibilias

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class HomeBanner(
    @SerialName("code")
    val code: Int = 0,
    @SerialName("dataList")
    val dataList: List<String> = listOf(),
    @SerialName("failToast")
    val failToast: List<String> = listOf(),
    @SerialName("imgUrlList")
    val imgUrlList: List<String> = listOf(),
    @SerialName("postData")
    val postData: List<String> = listOf(),
    @SerialName("successToast")
    val successToast: List<String> = listOf(),
    @SerialName("textList")
    val textList: List<String> = listOf(),
    @SerialName("time")
    val time: Int = 0,
    @SerialName("token")
    val token: List<String> = listOf(),
    @SerialName("typeList")
    val typeList: List<String> = listOf(),
)

package com.imcys.bilibilias.home.ui.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable


data class AppVersionsBean(
    @SerializedName("code")
    val code: Int, // 0
    @SerializedName("data")
    val `data`: Data,
    @SerializedName("msg")
    val msg: String, // 查询成功
) : Serializable {
    data class Data(
        @SerializedName("appMd5")
        val appMd5: String, // 0
        @SerializedName("appToken")
        val appToken: String, // 0
        @SerializedName("appTokenCr")
        val appTokenCr: String, // 0
        @SerializedName("appVersion")
        val appVersion: String, // 3.2
        @SerializedName("homeNotice")
        val homeNotice: String, // 如果你不喜欢该版本广告，请去设置关闭，bilibilias并不想消磨大家的耐心，大家可以去软件社区/GitHub反馈广告意见，这会关系到正式版本是否存在这样的广告。此版本GitHub已经推送了代码，见imcys或者beta分支。
        @SerializedName("id")
        val id: Int, // 17
        @SerializedName("model")
        val model: String, // 2
        @SerializedName("newVersion")
        val newVersion: String, // 3.2
        @SerializedName("upNotice")
        val upNotice: String, // 无
        @SerializedName("upUrl")
        val upUrl: String, // https://api.misakamoe.com/app/
    ) : Serializable
}